package exe7.criptographicBlocks;

import javafx.util.Pair;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class AsymmetricDecipher extends Block {

    private static final String CONFIGURATION_FILE = "ASYMDecipherConfiguration.txt";
    private static final String KEYPRIMITIVE = "keyPrimitive";
    public static final String KEYSTORE = "keyStore";

    public AsymmetricDecipher() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        super(CONFIGURATION_FILE);
    }

    public void init(PrivateKey key) throws InvalidKeyException {
        cipher.init(Cipher.UNWRAP_MODE, key);
    }

    public Pair<SecretKey, byte[]> execute(String fileName) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(fileName))){
            int ivSize = in.readInt();
            byte[] iv = new byte[ivSize];
            in.read(iv);
            byte[] key = new byte[in.available()];
            in.read(key);
            return new Pair<>((SecretKey) cipher.unwrap(key, get(KEYPRIMITIVE), Cipher.SECRET_KEY), iv);
        }
    }
}