import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashApp {
    public static void main(String[] args) {
        byte[] digest = getNBitsFromHash(args[0], Integer.parseInt(args[1]));

        System.out.print("SHA1(" + args[0] + ") = ");
        for (byte aDigest : digest) {
            System.out.print(String.format("%1$02x", aDigest));
        }
    }

    public static byte[] getNBitsFromHash(String arg, int i) {
        try {
            return trim(hashing(arg), i);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] hashing(String fileName) throws IOException, NoSuchAlgorithmException {
        FileInputStream fi = new FileInputStream(fileName);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        int nread = 0;
        byte buf[] = new byte[1024];
        while ((nread = fi.read(buf)) != -1) {
            sha.update(buf, 0, nread);
        }
        return sha.digest();
    }

    public static byte[] trim(byte[] array, int size){
        int rest = size % 8;
        int length = (size / 8) + (rest == 0 ? 0 : 1);

        byte[] ret = new byte[length];
        System.arraycopy(array, 0, ret, 0, length);

        if(rest != 0) {
            byte mask = (byte) ~(~0 << rest);
            ret[length - 1] = (byte) (ret[length - 1] & mask);
        }
        return ret;
    }
}