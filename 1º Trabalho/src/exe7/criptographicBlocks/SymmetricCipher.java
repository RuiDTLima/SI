package exe7.criptographicBlocks;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricCipher extends SymmetricBlock{
    public static final String CONFIGURATION_FILE = "SYMConfiguration.txt";

    public byte[] getIV(){
        return cipher.getIV();
    }

    public SymmetricCipher() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        super(CONFIGURATION_FILE);
    }

    public void init(SecretKey key) throws InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, key);
    }
}