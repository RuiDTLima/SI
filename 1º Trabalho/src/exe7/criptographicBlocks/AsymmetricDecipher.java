package exe7.criptographicBlocks;

import javafx.util.Pair;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import static exe7.IO.loadConfiguration;
import static javax.crypto.Cipher.SECRET_KEY;

public class AsymmetricDecipher {
    private final HashMap<String, String> configuration;
    private Cipher cipher;
    public static final String CONFIGURATION_FILE = "ASYMDecipherConfiguration.txt", PRIMITIVE = "primitive";

    public HashMap<String, String> getConfiguration() {
        return configuration;
    }

    public AsymmetricDecipher() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        configuration = loadConfiguration(CONFIGURATION_FILE);
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE));
    }

    public void init(PrivateKey key) throws InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key);
    }

    public Pair<SecretKey, byte[]> execute(String fileName) throws NoSuchAlgorithmException, InvalidKeyException {
        try (DataInputStream reader = new DataInputStream(new FileInputStream(fileName))){
            int ivSize = reader.readInt();

            byte[] iv = new byte[ivSize];
            reader.read(iv);

            byte[] key = new byte[reader.available()];
            reader.read(key);

            return new Pair<>((SecretKey) cipher.unwrap(key, configuration.get(PRIMITIVE), SECRET_KEY), iv);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}