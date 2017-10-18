package exe7.criptographicBlocks;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricCipher extends SymmetricBlock {

    private static final String CONFIGURATION_FILE = "SYMConfiguration.txt";

    public byte[] getIV(){
        return cipher.getIV();
    }

    public SymmetricCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        super(CONFIGURATION_FILE);
    }

    public void init(SecretKey key) throws InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}