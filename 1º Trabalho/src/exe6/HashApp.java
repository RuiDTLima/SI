package exe6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashApp {

    public static void main(String[] args) {
        try {
            String fileName = args[0], numberOfBits = args[1];
            byte[] hash = getTrimmedHash(loadFile(fileName), Integer.parseInt(numberOfBits));
            System.out.print(String.format("SHA1(%s) = ", fileName));
            for (byte hashByte : hash)
                System.out.print(String.format("%1$02x", hashByte));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] loadFile(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(fileName));
    }

    public static byte[] getTrimmedHash(byte[] file, int nbits) throws NoSuchAlgorithmException {
        return trim(getHash(file), nbits);
    }

    private static byte[] getHash(byte[] file) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-1").digest(file);
    }

    private static byte[] trim(byte[] array, int nbits){
        int rest = nbits % 8;
        int length = (nbits / 8) + (rest == 0 ? 0 : 1);
        byte[] ret = Arrays.copyOf(array, length);
        if(rest != 0)
            ret[length - 1] &= ~(~0 << rest);
        return ret;
    }
}