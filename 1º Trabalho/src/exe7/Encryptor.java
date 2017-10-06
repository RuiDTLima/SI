package exe7;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Encryptor extends CustomCipher {
    public void init(HashMap<String, String> configuration) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE) + "/" + configuration.get(OPERATIONMODE) + "/" + configuration.get(PADDINGMODE));
        KeyGenerator gen = KeyGenerator.getInstance(configuration.get(PRIMITIVE));
        key = gen.generateKey();
        privateKey = key; // teste
        cipher.init(Cipher.ENCRYPT_MODE, key);
        privateIv = getIV(); // teste
    }
}