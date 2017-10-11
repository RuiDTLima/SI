package exe7.criptographicBlocks;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import static exe7.IO.loadConfiguration;

public class SymmetricBlock {
    private final HashMap<String, String> configuration;
    protected Cipher cipher;
    public static final String PRIMITIVE = "primitive", OPERATIONMODE = "operationMode", PADDINGMODE = "paddingMode";

    public HashMap<String, String> getConfiguration() {
        return configuration;
    }

    public SymmetricBlock(String fileName) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException {
        configuration = loadConfiguration(fileName);
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE) + "/" + configuration.get(OPERATIONMODE) + "/" + configuration.get(PADDINGMODE));
    }

    public void execute(String fileNameIn, String fileNameOut) throws BadPaddingException, IllegalBlockSizeException {
        try (FileInputStream reader = new FileInputStream(fileNameIn);
             FileOutputStream writer = new FileOutputStream(fileNameOut)){
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = reader.read(buffer)) != -1) {
                writer.write(cipher.update(buffer, 0, nread));
            }
            writer.write(cipher.doFinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
