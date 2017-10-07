package exe7;

import javax.crypto.*;
import java.io.IOException;
import java.security.*;
import java.util.HashMap;

public class Encryptor extends CustomCipher {
    public void initSymmetric(HashMap<String, String> configuration, SecretKey key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE) + "/" + configuration.get(OPERATIONMODE) + "/" + configuration.get(PADDINGMODE));
        cipher.init(Cipher.ENCRYPT_MODE, key);
        privateIv = getIV(); // teste
    }

    public void initAsymmetric(HashMap<String, String> configuration, PublicKey key) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        cipher = Cipher.getInstance(configuration.get(PRIMITIVE));
        cipher.init(Cipher.WRAP_MODE, key);
    }
}