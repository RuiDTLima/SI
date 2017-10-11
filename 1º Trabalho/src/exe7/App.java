package exe7;

import exe7.criptographicBlocks.AsymmetricCipher;
import exe7.criptographicBlocks.AsymmetricDecipher;
import exe7.criptographicBlocks.SymmetricCipher;
import exe7.criptographicBlocks.SymmetricDecipher;
import javafx.util.Pair;
import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class App {
    private static final String  RESULTFILE = "Result.txt", METADATAFILE = "MetaData.txt",
                                CERTIFICATESFILE = "Certificates.txt", CIPHERED_FILE = "Ciphered_File.txt";

    public static void main(String[] args) {
        try {
            run("teste.txt", "cipher");
            run(CIPHERED_FILE + args[0], "decipher");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void run(String fileName, String operation) throws Exception {
        switch (operation){
            case "cipher": cipherMode(fileName); break;
            case "decipher": decipherMode(fileName); break;
            default: throw new Exception("Operation not supported");
        }
    }

    private static void cipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, CertificateException {
        SymmetricCipher symCipher = new SymmetricCipher();
        SecretKey secretKey = generateKey(symCipher.getConfiguration().get(SymmetricCipher.PRIMITIVE));
        symCipher.init(secretKey);

        symCipher.execute(fileName, CIPHERED_FILE);    //TODO change file to write, alterar ficheiro para o qual se vai escrever

        AsymmetricCipher asymCipher = new AsymmetricCipher();

        asymCipher.init(GetPublicKey(asymCipher.getConfiguration().get(AsymmetricCipher.CERTIFICATE)));

        asymCipher.execute(secretKey, symCipher.getIV(), METADATAFILE);
    }

    private static SecretKey generateKey(String primitive) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(primitive).generateKey();
    }

    private static PublicKey GetPublicKey(String certificate) throws FileNotFoundException, CertificateException {
        FileInputStream fis = new FileInputStream(certificate);
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        Certificate cert = cf.generateCertificate(bis);
        return cert.getPublicKey();
    }

    private static void decipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        AsymmetricDecipher asymDecipher = new AsymmetricDecipher();
        PrivateKey key = getPrivateKey();

        asymDecipher.init(key);

        Pair<SecretKey, byte[]> pair = asymDecipher.execute(METADATAFILE);

        SymmetricDecipher symDecipher = new SymmetricDecipher();

        symDecipher.init(pair.getKey(), pair.getValue());
        symDecipher.execute(fileName, RESULTFILE); //TODO change file to write, alterar ficheiro para o qual se vai escrever
    }
}