package exe7.criptographicBlocks;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricDecipher extends SymmetricBlock {

    private static final String CONFIGURATION_FILE = "SYMConfiguration.txt";

    public SymmetricDecipher() throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        super(CONFIGURATION_FILE);
    }

    public void init(SecretKey key, byte[] iv) throws InvalidAlgorithmParameterException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    }
}