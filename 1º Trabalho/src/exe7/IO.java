package exe7;

import java.io.*;
import java.util.HashMap;

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

    /**
     * Read the settings described in configuration file into a map.
     * @param fileName Name of the file to read from.
     * @return HashMap<String, String> containing all the settings.
     * @throws IOException
     */
    public static HashMap<String, String> loadConfiguration(String fileName) throws IOException {
        HashMap<String, String> configs = new HashMap<>();
        String toProcess = new String(IO.loadFile(fileName));

        String[] config = toProcess.split("\r\n");
        String[] temp;

        for (String curr : config) {
            temp = curr.split(":");
            configs.put(temp[0], temp[1]);
        }

        return configs;
    }
}