package exe7.criptographicBlocks;

import javax.crypto.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import static exe7.IO.loadConfiguration;

public class AsymmetricCipher {
    private final HashMap<String, String> configuration;
    private Cipher cipher;
    public static final String CONFIGURATION_FILE = "ASYMCipherConfiguration.txt", PRIMITIVE = "primitive", CERTIFICATE = "certificate", TRUST_ANCHOR = "trustAnchor";

    public HashMap<String, String> getConfiguration() {
        return configuration;
    }

    public AsymmetricCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        configuration = loadConfiguration(CONFIGURATION_FILE);
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE));
    }

    public void init(PublicKey key) throws InvalidKeyException {
        cipher.init(Cipher.WRAP_MODE, key);
    }

    public void execute(SecretKey key, byte[] iv, String fileNameOut) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, FileNotFoundException {
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileNameOut))){
            writer.writeInt(iv.length);
            writer.write(iv);
            writer.write(cipher.wrap(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
