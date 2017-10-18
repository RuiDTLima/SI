package exe7.criptographicBlocks;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class SymmetricBlock extends Block {

    private static final String OPERATIONMODE = "operationMode";
    private static final String PADDINGMODE = "paddingMode";

    protected SymmetricBlock(String fileName) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException {
        super(fileName, OPERATIONMODE, PADDINGMODE);
    }

    public void execute(String fileNameIn, String fileNameOut) throws IOException, BadPaddingException, IllegalBlockSizeException {
        try (FileInputStream in = new FileInputStream(fileNameIn);
             FileOutputStream out = new FileOutputStream(fileNameOut)){
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = in.read(buffer)) != -1)
                out.write(cipher.update(buffer, 0, nread));
            out.write(cipher.doFinal());
        }
    }
}
