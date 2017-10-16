package exe7.criptographicBlocks;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricDecipher extends SymmetricBlock{
    public static final String CONFIGURATION_FILE = "SYMConfiguration.txt";

    public SymmetricDecipher() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        super(CONFIGURATION_FILE);
    }

    public void init(SecretKey key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
    }
}