package exe7;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Decryptor extends CustomCipher {
    public void init(HashMap<String, String> configuration, SecretKey key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE) + "/" + configuration.get(OPERATIONMODE) + "/" + configuration.get(PADDINGMODE));
        //this.key = privateKey; // teste
        cipher.init(Cipher.DECRYPT_MODE, privateKey, new IvParameterSpec(privateIv));
    }
}