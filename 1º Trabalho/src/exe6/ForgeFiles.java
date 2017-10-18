package exe6;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class ForgeFiles {

    private static final int MIN = 32;
    private static final int MAX = 126;
    private static final int ITERATIONS = 5;
    private static final Random RND = new Random();
    private static byte[] file1, file2;
    private static HashMap<String, Integer> hash1 = new HashMap<>(), hash2 = new HashMap<>();

    public static void main(String[] args){
        try {
            String fileName1 = args[0], fileName2 = args[1], numberOfBits = args[2];
            int nbits = Integer.parseInt(numberOfBits), total = 0;

            byte[] original1 = HashApp.loadFile(fileName1),
                    original2 = HashApp.loadFile(fileName2),
                    hashFile1 = HashApp.getTrimmedHash(original1, nbits),
                    hashFile2 = HashApp.getTrimmedHash(original2, nbits);

            if (Arrays.equals(hashFile1, hashFile2))
                System.out.println("Given files already have same hash.");
            else {
                for (int i = 0; i < ITERATIONS; i++) {
                    int j = 0;
                    file1 = ForgeFile.alterFile(original1, "//".getBytes());
                    file2 = ForgeFile.alterFile(original2, "//".getBytes());
                    hash1.clear();
                    hash2.clear();
                    do {
                        file1 = ForgeFile.alterFile(file1, (byte) (MIN + (RND.nextInt(MAX - MIN))));
                        file2 = ForgeFile.alterFile(file2, (byte) (MIN + (RND.nextInt(MAX - MIN))));
                        hashFile1 = HashApp.getTrimmedHash(file1, nbits);
                        hashFile2 = HashApp.getTrimmedHash(file2, nbits);
                    } while (!compare(hashFile1, hashFile2, j++));
                    total += j;
                    saveFiles(file1, file2, i);
                }
                System.out.println(String.format("Average number of tries till equal hashes = %d", total / ITERATIONS));
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static boolean compare(byte[] hashFile1, byte[] hashFile2, int i) {
        String h1 = Arrays.toString(hashFile1), h2 = Arrays.toString(hashFile2);
        if (hash1.containsKey(h2)) {
            file1 = Arrays.copyOf(file1, file1.length - (i - hash1.get(h2)));
            return true;
        }
        if (hash2.containsKey(h1)) {
            file2 = Arrays.copyOf(file2, file2.length - (i - hash2.get(h1)));
            return true;
        }
        hash1.put(h1, i);
        hash2.put(h2, i);
        return false;
    }

    private static void saveFiles(byte[] file1, byte[] file2, int i) throws IOException {
        try (FileOutputStream out1 = new FileOutputStream(String.format("File1_%d.java", i));
             FileOutputStream out2 = new FileOutputStream(String.format("File2_%d.java", i))) {
            out1.write(file1);
            out2.write(file2);
        }
    }
}