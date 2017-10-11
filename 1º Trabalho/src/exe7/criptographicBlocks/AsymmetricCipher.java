package exe7.criptographicBlocks;

import javax.crypto.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.HashMap;
import static exe7.IO.loadConfiguration;

public class AsymmetricCipher {
    private final HashMap<String, String> configuration;
    private Cipher cipher;
    public static final String CONFIGURATION_FILE = "ASYMCipherConfiguration.txt", PRIMITIVE = "primitive", CERTIFICATE = "certificate";

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

    public void execute(SecretKey key, byte[] iv, String fileNameOut) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        try (FileOutputStream writer = new FileOutputStream(fileNameOut)){
            writer.write(iv.length);
            writer.write(iv);
            writer.write(cipher.wrap(key));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
