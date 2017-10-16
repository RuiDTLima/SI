package exe6;

import java.io.FileOutputStream;
import java.util.Random;

public class ForgeFile {
    private static final int MIN = 32;
    private static final int MAX = 126;
    private static final int ITERATIONS = 5;
    private static final Random RND = new Random();

    public static void main(String[] args){
        int size = Integer.parseInt(args[2]), count = 0;

        byte[] file = HashApp.loadFile(args[0]);

        byte[] hashFile1 = HashApp.getNBitsFromHash(file, size); // BadApp
        byte[] hashFile2 = HashApp.getNBitsFromHash(HashApp.loadFile(args[1]), size); // GoodApp

        if (compare(hashFile1, hashFile2))
            System.out.println(1);

        else {
            for (int i = 0; i < ITERATIONS; i++) {
                file = HashApp.loadFile(args[0]);
                file = alterFile(file, "//".getBytes());

                while (!compare(hashFile1, hashFile2)) {
                    count++;
                    file = alterFile(file, (byte) (MIN + (RND.nextInt(MAX - MIN))));
                    hashFile1 = HashApp.getNBitsFromHash(file, size);
                }
                saveFile(file, i);
                hashFile1 = HashApp.getNBitsFromHash(file, size);
            }

            System.out.println(count / ITERATIONS);
        }
    }

    private static void saveFile(byte[] file, int i) {
        try (FileOutputStream newFile = new FileOutputStream("File_" + i + ".java")) {
            newFile.write(file);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] alterFile(byte[] file, byte... toAdd) {
        byte[] newFile = new byte[file.length + toAdd.length];
        System.arraycopy(file, 0, newFile, 0, file.length);
        System.arraycopy(toAdd, 0, newFile, file.length, toAdd.length);
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