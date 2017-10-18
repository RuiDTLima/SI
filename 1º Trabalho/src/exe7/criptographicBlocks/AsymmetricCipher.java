package exe7.criptographicBlocks;

import javax.crypto.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class AsymmetricCipher extends Block {

    private static final String CONFIGURATION_FILE = "ASYMCipherConfiguration.txt";
    public static final String CERTIFICATE = "certificate";
    public static final String TRUST_ANCHOR = "trustAnchor";

    public AsymmetricCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        super(CONFIGURATION_FILE);
    }

    public void init(PublicKey key) throws InvalidKeyException {
        cipher.init(Cipher.WRAP_MODE, key);
    }

    public void execute(SecretKey key, byte[] iv, String fileNameOut) throws IOException, InvalidKeyException, IllegalBlockSizeException {
        try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(fileNameOut))){
            writer.writeInt(iv.length);
            writer.write(iv);
            writer.write(cipher.wrap(key));
        }
    }
}
