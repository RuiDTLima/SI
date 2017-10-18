package exe6;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class ForgeFile {

    private static final int MIN = 32;
    private static final int MAX = 126;
    private static final int ITERATIONS = 5;
    private static final Random RND = new Random();

    public static void main(String[] args){
        try {
            String badFileName = args[0], goodFileName = args[1], numberOfBits = args[2];
            int nbits = Integer.parseInt(numberOfBits), total = 0;

            byte[] originalBadFile = HashApp.loadFile(badFileName),
                    badFileHash = HashApp.getTrimmedHash(originalBadFile, nbits),
                    goodFileHash = HashApp.getTrimmedHash(HashApp.loadFile(goodFileName), nbits);

            if (Arrays.equals(badFileHash, goodFileHash))
                System.out.println("Given files already have same hash.");
            else {
                for (int i = 0; i < ITERATIONS; i++) {
                    byte[] badFile =  alterFile(originalBadFile, "//".getBytes());
                    do {
                        badFile = alterFile(badFile, (byte) (MIN + (RND.nextInt(MAX - MIN))));
                        badFileHash = HashApp.getTrimmedHash(badFile, nbits);
                        total++;
                    } while (!Arrays.equals(badFileHash, goodFileHash));
                    saveFile(badFile, i);
                }
                System.out.println(String.format("Average number of tries till equal hashes = %d", total / ITERATIONS));
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] alterFile(byte[] file, byte... toAdd) {
        byte[] newFile = new byte[file.length + toAdd.length];
        System.arraycopy(file, 0, newFile, 0, file.length);
        System.arraycopy(toAdd, 0, newFile, file.length, toAdd.length);
        return newFile;
    }

    private static void saveFile(byte[] file, int i) throws IOException {
        try (FileOutputStream out = new FileOutputStream(String.format("File_%d.java", i))) {
            out.write(file);
        }
    }
}