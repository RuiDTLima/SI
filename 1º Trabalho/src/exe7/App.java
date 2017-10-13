package exe7;

import exe7.criptographicBlocks.AsymmetricCipher;
import exe7.criptographicBlocks.AsymmetricDecipher;
import exe7.criptographicBlocks.SymmetricCipher;
import exe7.criptographicBlocks.SymmetricDecipher;
import javafx.util.Pair;
import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;

public class App {
    private static final String  RESULTFILE = "Result.txt", METADATAFILE = "MetaData.txt",
                                CERTIFICATESFILE = "Certificates.txt", CIPHERED_FILE = "Ciphered_File.txt";
    private static final char[] PASSWORD = "changeit".toCharArray();

    public static void main(String[] args) {
        try {
            run("teste.txt", "cipher");
            run(CIPHERED_FILE, "decipher");
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

    private static void cipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, CertificateException, KeyStoreException {
        SymmetricCipher symCipher = new SymmetricCipher();
        SecretKey secretKey = generateKey(symCipher.getConfiguration().get(SymmetricCipher.PRIMITIVE));
        symCipher.init(secretKey);

        symCipher.execute(fileName, CIPHERED_FILE);    //TODO change file to write, alterar ficheiro para o qual se vai escrever

        AsymmetricCipher asymCipher = new AsymmetricCipher();

        // asymCipher.init(GetPublicKey(asymCipher.getConfiguration().get(AsymmetricCipher.CERTIFICATE)));

        asymCipher.execute(secretKey, symCipher.getIV(), METADATAFILE);
    }

    private static SecretKey generateKey(String primitive) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(primitive).generateKey();
    }

    private static PublicKey GetPublicKey(String certificate) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CertPathBuilderException {

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        KeyStore trustAnchor1 = loadTrustAnchor("trust.anchors/CA1.jks"), trustAnchor2 = loadTrustAnchor("trust.anchors/CA2.jks");

        X509CertSelector certSelector = new X509CertSelector();
        certSelector.setSubject(certificate);

        PKIXBuilderParameters parameters = new PKIXBuilderParameters(trustAnchor1, certSelector);
        CertPathBuilderResult result = CertPathBuilder.getInstance("PKIX")
                                                      .build(parameters);
        CollectionCertStoreParameters certStoreParameters = new CollectionCertStoreParameters(loadIntermediateCertificates(cf));

        Certificate cert = loadCertificate(cf, certificate);
        return cert.getPublicKey();
    }

    private static Certificate loadCertificate(CertificateFactory certificateFactory, String certificateName ) throws IOException, CertificateException {
        Certificate cert;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(certificateName))){
            cert = certificateFactory.generateCertificate(bis);
        }
        return cert;
    }

    private static Collection<Certificate> loadIntermediateCertificates(CertificateFactory certificateFactory) throws CertificateException, IOException {
        File folder = new File("cert.CAintermedia");
        File[] listOfFiles = folder.listFiles();
        ArrayList<Certificate> certificates = new ArrayList<>(); // Assume-se que no package apenas existem certificados

        for (File listOfFile : listOfFiles) {
            certificates.add(loadCertificate(certificateFactory, listOfFile.getName()));
        }

        return certificates;
    }

    private static KeyStore loadTrustAnchor(String certificateName) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        try(FileInputStream fis = new FileInputStream(certificateName)){
            KeyStore trustAnchor1 = KeyStore.getInstance("PKIX");
            trustAnchor1.load(fis, PASSWORD);
            return trustAnchor1;
        }
    }

    private static void decipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, UnrecoverableKeyException {
        AsymmetricDecipher asymDecipher = new AsymmetricDecipher();
        PrivateKey key = getPrivateKey();

        asymDecipher.init(key);
        Pair<SecretKey, byte[]> pair = asymDecipher.execute(METADATAFILE);

        SymmetricDecipher symDecipher = new SymmetricDecipher();

        symDecipher.init(pair.getKey(), pair.getValue());
        symDecipher.execute(fileName, RESULTFILE); //TODO change file to write, alterar ficheiro para o qual se vai escrever
    }

    private static PrivateKey getPrivateKey() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new java.io.FileInputStream("pfx/Alice_1.pfx");   //TODO ler do ficheiro
        ks.load(fis, PASSWORD);
        fis.close();
        return (PrivateKey)ks.getKey("1", PASSWORD);
    }
}