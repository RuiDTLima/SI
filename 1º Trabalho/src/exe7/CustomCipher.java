package exe7;

import javax.crypto.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;

public class CustomCipher {
    public static final String PRIMITIVE = "primitive", OPERATIONMODE = "operationMode", PADDINGMODE= "paddingMode";
    protected Cipher cipher;
    public static SecretKey privateKey;
    public static byte[] privateIv;

    /**
     * Process the message either to encrypt or decrypt depending on the object configuration.
     * @param fileNameIn Information to process.
     * @param fileNameOut
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public void process(String fileNameIn, String fileNameOut) throws BadPaddingException, IllegalBlockSizeException {
        try (FileInputStream reader = new FileInputStream(fileNameIn);
             FileOutputStream writer = new FileOutputStream(fileNameOut)){
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = reader.read(buffer)) != -1) {
                writer.write(cipher.update(buffer, 0, nread));
            }
            writer.write(cipher.doFinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(SecretKey key, byte[] privateIv, String fileNameOut) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        try (FileOutputStream writer = new FileOutputStream(fileNameOut)){
            writer.write(cipher.wrap(key));
            writer.write(privateIv);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getIV(){
        return cipher.getIV();
    }
}