package exe7.criptographicBlocks;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

public class Block {

    public static final String PRIMITIVE = "primitive";

    private final HashMap<String, String> configuration;
    protected Cipher cipher;

    protected Block(String fileName, String... params) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        configuration = loadConfiguration(fileName);
         StringBuilder sb = new StringBuilder(configuration.get(PRIMITIVE));
        for (String param : params)
            sb.append('/').append(configuration.get(param));
        cipher = Cipher.getInstance(sb.toString());
    }

    public String get(String key) {
        return configuration.get(key);
    }

    private static HashMap<String, String> loadConfiguration(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        HashMap<String, String> configs = new HashMap<>();
        for (String line : lines) {
            String[] pair = line.split(":");
            configs.put(pair[0], pair[1]);
        }
        return configs;
    }
}
