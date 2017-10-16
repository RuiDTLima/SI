package exe7;

import java.io.*;
import java.util.HashMap;

public class IO {
    /**
     * Read the settings described in configuration file into a map.
     * @param fileName Name of the file to read from.
     * @return HashMap<String, String> containing all the settings.
     * @throws IOException
     */
    public static HashMap<String, String> loadConfiguration(String fileName) throws IOException {
        FileInputStream reader = new FileInputStream(fileName);
        byte[] read = new byte[reader.available()];
        reader.read(read);


        HashMap<String, String> configs = new HashMap<>();
        String toProcess = new String(read);

        String[] config = toProcess.split("\r\n");
        String[] temp;

        for (String curr : config) {
            temp = curr.split(":");
            configs.put(temp[0], temp[1]);
        }

        return configs;
    }
}