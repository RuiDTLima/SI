import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class ForgeFiles {
    private static final int MIN = 32, MAX = 126;
    private static final Random RND = new Random();
    private static byte[] file1, file2;
    private static HashMap<String, Integer> hash1 = new HashMap<>(), hash2 = new HashMap<>();

    public static void main(String[] args){
        int size = Integer.parseInt(args[2]), count = 0;

        file1 = HashApp.loadFile(args[0]);
        file2 = HashApp.loadFile(args[1]);
        byte[] original1 = file1.clone(), original2 = file2.clone();

        String hashFile1 = getHash(file1, size), hashFile2 = getHash(file2, size); // GoodApp

        if (hashFile1.equals(hashFile2))
            System.out.println(1);
        else {
            int j;
            for (int i = 0; i < 5; i++) {
                j = 0;
                do {
                    count++;
                    file1 = alterFile(file1);
                    file2 = alterFile(file2);
                    hashFile1 = getHash(file1, size);
                    hashFile2 = getHash(file2, size);
                } while (!compare(hashFile1, hashFile2, j++));

                saveFiles(file1, file2, i);

                reset(original1, original2);
            }

            System.out.println(count / 5);
        }
    }

    private static void reset(byte[] original1, byte[] original2) {
        file1 = original1.clone();
        file2 = original2.clone();
        hash1.clear();
        hash2.clear();
    }

    private static void saveFiles(byte[] file1, byte[] file2, int i) {
        try (FileOutputStream newFile1 = new FileOutputStream("File1_" + i + ".java");
             FileOutputStream newFile2 = new FileOutputStream("File2_" + i + ".java")) {
            newFile1.write(file1);
            newFile2.write(file2);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean compare(String hashFile1, String hashFile2, int i) {
        if (hash1.containsKey(hashFile2)) {
            file1 = Arrays.copyOf(file1, file1.length - (i - hash1.get(hashFile2)));
            return true;
        }
        if (hash2.containsKey(hashFile1)) {
            file2 = Arrays.copyOf(file2, file2.length - (i - hash2.get(hashFile1)));
            return true;
        }

        hash1.put(hashFile1, i);
        hash2.put(hashFile2, i);
        return false;
    }

    private static byte[] alterFile(byte[] file)  {
        int randomNum = (MIN + (RND.nextInt(MAX - MIN)));
        byte[] newFile = new byte[file.length + 1];
        System.arraycopy(file, 0, newFile, 0, file.length);
        newFile[newFile.length - 1] = (byte) randomNum;
        return newFile;
    }

    private static String getHash(byte[] file, int size) {
        return convert(HashApp.getNBitsFromHash(file, size));
    }

    private static String convert(byte[] byteFile) {
        StringBuilder f = new StringBuilder();
        for (byte b : byteFile)
            f.append(b);
        return f.toString();
    }
}