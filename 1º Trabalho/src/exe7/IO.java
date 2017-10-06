package exe7;

import java.io.*;

public class IO {
    /**
     * Write to the specified file then content.
     * @param fileName File's name.
     * @param content Content to be written.
     * @throws IOException
     */
    public static void writeFile(String fileName, byte[] content) throws IOException {
        FileOutputStream writer = new FileOutputStream(fileName);
        writer.write(content);
        writer.close();
    }

    /**
     * Read from the specified file.
     * @param fileName File's name.
     * @return Content read from file.
     * @throws IOException
     */
    public static byte[] loadFile(String fileName) throws IOException {
        FileInputStream reader = new FileInputStream(fileName);
        byte[] read = new byte[reader.available()];
        reader.read(read);
        return read;
    }
}