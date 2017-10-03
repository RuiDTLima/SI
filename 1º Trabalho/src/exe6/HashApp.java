package exe6;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashApp {
    public static void main(String[] args) {
        byte[] file = loadFile(args[0]);
        byte[] digest = getNBitsFromHash(file, Integer.parseInt(args[1]));
        System.out.print("SHA1(" + args[0] + ") = ");
        for (byte aDigest : digest)
            System.out.print(String.format("%1$02x", aDigest));
    }

    public static byte[] getNBitsFromHash(byte[] file, int n) {
        return trim(hashing(file), n);
    }

    public static byte[] hashing(byte[] file) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sha.update(file);

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

    public static byte[] loadFile(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}