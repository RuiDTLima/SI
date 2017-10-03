package exe7;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Cypher {
    public static void cypherFile(String fileName, SecretKey key){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] msg = Files.readAllBytes(Paths.get(fileName));
            byte[] cryptogram = cipher.doFinal(msg);

            System.out.println("Plain text length = " + msg.length);
            System.out.println("Cipher text length = " + cryptogram.length);

            FileOutputStream writer = new FileOutputStream("cifra.txt");
            writer.write(cryptogram);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        SecretKey key = gen.generateKey();
        //cypherFile("ola.txt", key);
        Decypher.decypherFile("cifra.txt", key);
    }
}