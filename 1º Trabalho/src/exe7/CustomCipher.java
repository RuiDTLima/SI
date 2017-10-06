package exe7;

import javax.crypto.*;
import java.io.IOException;

public class CustomCipher {
    protected final String PRIMITIVE = "primitive", OPERATIONMODE = "operationMode", PADDINGMODE= "paddingMode";
    protected SecretKey key;
    protected Cipher cipher;
    public static SecretKey privateKey;
    public static byte[] privateIv;

    /**
     * Process the message either to encrypt or decrypt depending on the object configuration.
     * @param message Information to process.
     * @return String with the message process result.
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public byte[] process(byte[] message) throws IOException, BadPaddingException, IllegalBlockSizeException {
       return cipher.doFinal(message);
    }

    public byte[] getIV(){
        return cipher.getIV();
    }

    public SecretKey getKey() {
        return key;
    }
}