package exe6;

import java.util.Random;

public class ForgeFile {
    private static final int MIN = 32;
    private static final int MAX = 126;
    private static final Random RND = new Random();

    public static void main(String[] args){
        int size = Integer.parseInt(args[2]), count = 0;

        byte[] file = HashApp.loadFile(args[0]);

        byte[] hashFile1 = HashApp.getNBitsFromHash(file, size); // BadApp
        byte[] hashFile2 = HashApp.getNBitsFromHash(HashApp.loadFile(args[1]), size); // GoodApp

        if (compare(hashFile1, hashFile2))
            System.out.println(1);

        else {
            for (int i = 0; i < 5; i++) {
                file = HashApp.loadFile(args[0]);

                while (!compare(hashFile1, hashFile2)) {
                    count++;
                    file = alterFile(file);
                    hashFile1 = HashApp.getNBitsFromHash(file, size);
                }
                hashFile1 = HashApp.getNBitsFromHash(file, size);
            }

            System.out.println(count / 5);
        }
    }

    private static byte[] alterFile(byte[] file)  {
        int randomNum = (MIN + (RND.nextInt(MAX - MIN)));
        byte[] newFile = new byte[file.length + 1];
        System.arraycopy(file, 0, newFile, 0, file.length);
        newFile[newFile.length - 1] = (byte) randomNum;
        return newFile;
    }

    private static boolean compare(byte[] hash1, byte[] hash2){
        if (hash1.length != hash2.length)
            return false;

        for (int i = 0; i < hash1.length; i++) {
            if (hash1[i] != hash2[i])
                return false;
        }
        return true;
    }
}