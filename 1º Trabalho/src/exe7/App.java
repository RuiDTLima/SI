package exe7;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;

public class App {
    public static final String ENCRYPTEDFILE = "EncryptedMessage.txt", RESULTFILE = "Result.txt",
                                SYMCONFIGURATION = "SYMConfiguration.txt", ASYMCONFIGURATION = "ASYMConfiguration.txt",
                                METADATAFILE = "MetaData.txt",  CERTIFICATESFILE = "Certificates.txt";

    public static void main(String[] args) {
        try {
            run("teste.txt", "cipher");
            run("EncryptedMessage.txt", "decipher");
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
        HashMap<String, String> configuration = loadConfiguration(SYMCONFIGURATION);

        SecretKey symmetricKey = generateKey(configuration);

        symmetricCipher(fileName, symmetricKey);

        PublicKey publicKey = GetPublicKey();

        asymmetricCypher(symmetricKey, publicKey);
    }

    private static void asymmetricCypher(SecretKey symmetricKey, PublicKey publicKey) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        Encryptor ecp = new Encryptor();
        ecp.initAsymmetric(loadConfiguration(ASYMCONFIGURATION), publicKey);
        ecp.process(symmetricKey, CustomCipher.privateIv, METADATAFILE);
    }

    private static SecretKey generateKey(HashMap<String, String> configuration) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(configuration.get(CustomCipher.PRIMITIVE)).generateKey();
    }

    private static PublicKey GetPublicKey() throws FileNotFoundException, CertificateException {
        FileInputStream fis = new FileInputStream("cert.end.entities/Alice_1.cer");
        BufferedInputStream bis = new BufferedInputStream(fis);

        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        Certificate cert = cf.generateCertificate(bis);
        return cert.getPublicKey();
    }

    private static void decipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        // load key store containing private key and meta data
        // get the key to decipher message
        //assymetricDecipher();

        // decipher the message using the key deciphered before

        symmetricDecipher(fileName, CustomCipher.privateKey);
    }

    /**
     * Create the encipher object with the settings defined on the configuration file and encrypts the message.
     * @param fileName Message to be encrypted.
     * @return SecretKey used to encrypt the message.
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static void symmetricCipher(String fileName, SecretKey key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Encryptor ecp = new Encryptor();
        ecp.initSymmetric(loadConfiguration(SYMCONFIGURATION), key);

        ecp.process(fileName, ENCRYPTEDFILE);
    }


    /**
     * Create the decipher object with the settings defined on the configuration file and decrypts the cryptogram.
     * @param fileName Message to be decrypted.
     * @param key SecretKey used to decrypt te message.
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static void symmetricDecipher(String fileName, SecretKey key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Decryptor dcp = new Decryptor();
        dcp.init(loadConfiguration(SYMCONFIGURATION), key);

        dcp.process(fileName, RESULTFILE);
    }

    /**
     * Read the settings described in configuration file into a map.
     * @param fileName Name of the file to read from.
     * @return HashMap<String, String> containing all the settings.
     * @throws IOException
     */
    private static HashMap<String, String> loadConfiguration(String fileName) throws IOException {
        HashMap<String, String> configs = new HashMap<>();
        String toProcess = new String(IO.loadFile(fileName));

        String[] config = toProcess.split("\r\n");
        String[] temp;

        for (String curr : config) {
            temp = curr.split(":");
            configs.put(temp[0], temp[1]);
        }

        return configs;
    }
}