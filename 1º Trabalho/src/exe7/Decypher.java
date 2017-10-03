package exe7;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Decypher {
    public static void decypherFile(String fileName, SecretKey key){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] cryptogram = Files.readAllBytes(Paths.get(fileName));
            byte[] msg = cipher.doFinal(cryptogram);

            System.out.println("Plain text length = " + cryptogram.length);
            System.out.println("Cipher text length = " + msg.length);

            FileOutputStream writer = new FileOutputStream("decifra.txt");
            writer.write(msg);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }
}