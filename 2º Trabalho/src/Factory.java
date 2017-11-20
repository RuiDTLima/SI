import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class Factory {

    private static final char[] PASSWORD = "changeit".toCharArray();

    public static TrustManager[] getTrustManagers(String certificateName) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX");
        trustManagerFactory.init(getKeyStore(certificateName, "JKS"));
        return trustManagerFactory.getTrustManagers();
    }

    public static KeyManager[] getKeyManagers(String certificateName) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("PKIX");
        keyManagerFactory.init(getKeyStore(certificateName, "PKCS12"), PASSWORD);
        return keyManagerFactory.getKeyManagers();
    }

    private static KeyStore getKeyStore(String certificateName, String keyStore) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        try (FileInputStream in = new FileInputStream(certificateName)) {
            KeyStore trustAnchor = KeyStore.getInstance(keyStore);
            trustAnchor.load(in, PASSWORD);
            return  trustAnchor;
        }
    }
}
