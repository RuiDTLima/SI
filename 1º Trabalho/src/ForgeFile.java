import java.io.*;
import java.util.Random;

public class ForgeFile {
    private static final int MIN = 32;
    private static final int MAX = 126;
    private static final Random RND = new Random();

    public static void main(String[] args){
        String file1 = args[0], file2 = args[1], newFile = "App.java";
        int size = Integer.parseInt(args[2]), count = 0;

        byte[] hashFile2 = HashApp.getNBitsFromHash(file2, size); // GoodApp
        byte[] hashFile1 = HashApp.getNBitsFromHash(file1, size); // BadApp

        if (compare(hashFile1, hashFile2))
            System.out.println(1);

        else {
            for (int i = 0; i < 5; i++) {
                copyFile(file1, newFile);

                while (!compare(hashFile1, hashFile2)) {
                    count++;
                    alterFile(newFile);
                    hashFile1 = HashApp.getNBitsFromHash(newFile, size);
                }
                hashFile1 = HashApp.getNBitsFromHash(file1, size);
            }

            System.out.println(count / 5);
        }
    }

    private static void copyFile(String file, String newFile) {
        try (FileInputStream in = new FileInputStream(file);
             FileOutputStream out = new FileOutputStream(newFile)) {

            File oldfile = new File(file);
            byte[] data = new byte[(int) oldfile.length()];
            in.read(data);
            out.write(data);
            out.write(new byte[]{'/', '/'});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void alterFile(String file)  {
        int randomNum = (MIN + (RND.nextInt(MAX - MIN)));
        try (FileOutputStream out = new FileOutputStream(file, true)) {
            out.write(randomNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
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