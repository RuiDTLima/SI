package exe7;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    private static void cipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        SecretKey keyMessage = symmetricCipher(fileName);

        // wrap the created key

        // Certificate certificate = validateCertificates();
        // load root certificates into key, and loda other certificates to do certpath
        // validate certificates
        // cipher
        //assymetricCipher();
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
    private static SecretKey symmetricCipher(String fileName) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Encryptor ecp = new Encryptor();
        ecp.init(loadConfiguration(SYMCONFIGURATION));

        ecp.process(fileName, ENCRYPTEDFILE);

        return ecp.getKey();
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