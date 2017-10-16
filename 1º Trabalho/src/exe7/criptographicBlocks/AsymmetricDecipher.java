package exe7.criptographicBlocks;

import exe7.IO;
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

public class AsymmetricDecipher {
    private final HashMap<String, String> configuration;
    private Cipher cipher;
    public static final String CONFIGURATION_FILE = "ASYMDecipherConfiguration.txt", PRIMITIVE = "primitive",
                                KEYPRIMITIVE = "keyPrimitive", KEYSTORE = "keyStore";

    public HashMap<String, String> getConfiguration() {
        return configuration;
    }

    public AsymmetricDecipher() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        configuration = IO.loadConfiguration(CONFIGURATION_FILE);
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE));
    }

    public void init(PrivateKey key) throws InvalidKeyException {
        cipher.init(Cipher.UNWRAP_MODE, key);
    }

    public Pair<SecretKey, byte[]> execute(String fileName) throws NoSuchAlgorithmException, InvalidKeyException {
        try (DataInputStream reader = new DataInputStream(new FileInputStream(fileName))){
            int ivSize = reader.readInt();

            byte[] iv = new byte[ivSize];
            reader.read(iv);

            byte[] key = new byte[reader.available()];
            reader.read(key);

            return new Pair<>((SecretKey) cipher.unwrap(key, configuration.get(KEYPRIMITIVE), Cipher.SECRET_KEY), iv);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}