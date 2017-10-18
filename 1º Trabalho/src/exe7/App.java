package exe7;

import exe7.criptographicBlocks.*;
import javafx.util.Pair;

import javax.crypto.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;

public class App {

    private static final char[] PASSWORD = "changeit".toCharArray();

    public static void main(String[] args) {
        String fileName = args[0], operation = args[1];
        String[] components = fileName.split("[.]");
        String resultFile, metadataFile;
        try {
            long startTime = System.currentTimeMillis();
            if(operation.equalsIgnoreCase("cipher")) {
                resultFile = String.format("%sCiphered.%s", components[0], components[1]);
                metadataFile = String.format("%sMetaData.txt", components[0]);
                cipher(fileName, resultFile, metadataFile);
            } else if(operation.equalsIgnoreCase("decipher")) {
                resultFile = String.format("%sDeciphered.%s", components[0], components[1]);
                metadataFile = String.format("%sMetadata.txt", components[0].replace("Ciphered", ""));
                decipher(fileName, resultFile, metadataFile);
            }
            else {
                System.out.println("Invalid Operation.\n Valid operations are \"cipher\" and \"decipher\"");
                return;
            }
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Time elapsed in millis: %d", endTime - startTime));
        } catch (InvalidKeyException | InvalidAlgorithmParameterException |
                NoSuchAlgorithmException | KeyStoreException |
                CertPathBuilderException | CertificateException |
                UnrecoverableKeyException | IllegalBlockSizeException |
                BadPaddingException | IOException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    private static void cipher(String fileName, String resultFile, String metadataFile) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, CertificateException, CertPathBuilderException, InvalidAlgorithmParameterException, KeyStoreException {
        SymmetricCipher symCipher = new SymmetricCipher();
        SecretKey secretKey = generateKey(symCipher.get(Block.PRIMITIVE));
        symCipher.init(secretKey);
        symCipher.execute(fileName, resultFile);

        AsymmetricCipher asymCipher = new AsymmetricCipher();
        PublicKey publicKey = GetPublicKey(asymCipher.get(AsymmetricCipher.CERTIFICATE), asymCipher.get(AsymmetricCipher.TRUST_ANCHOR));
        asymCipher.init(publicKey);
        asymCipher.execute(secretKey, symCipher.getIV(), metadataFile);
    }

    private static SecretKey generateKey(String primitive) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(primitive).generateKey();
    }

    private static PublicKey GetPublicKey(String certificate, String trustAnchorName) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CertPathBuilderException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate leaf = (X509Certificate) loadCertificate(cf, certificate);
        KeyStore trustAnchor = loadTrustAnchor(trustAnchorName);

        X509CertSelector certSelector = new X509CertSelector();
        certSelector.setCertificate(leaf);

        PKIXBuilderParameters parameters = new PKIXBuilderParameters(trustAnchor, certSelector);
        CollectionCertStoreParameters certStoreParameters = new CollectionCertStoreParameters(getIntermediateAndLeafCertificates(cf, leaf));

        parameters.addCertStore(CertStore.getInstance("Collection", certStoreParameters));
        parameters.setRevocationEnabled(false);
        CertPathBuilder.getInstance("PKIX").build(parameters);

        return leaf.getPublicKey();
    }

    private static Collection<Certificate> getIntermediateAndLeafCertificates(CertificateFactory certificateFactory, X509Certificate leaf) throws IOException, CertificateException {
        File folder = new File("cert.CAintermedia");
        File[] listOfFiles = folder.listFiles();
        ArrayList<Certificate> certificates = new ArrayList<>();
        certificates.add(leaf);
        for (File file : listOfFiles)
            certificates.add(loadCertificate(certificateFactory, file.getPath()));
        return certificates;
    }

    private static Certificate loadCertificate(CertificateFactory certificateFactory, String certificateName ) throws IOException, CertificateException {
        try(FileInputStream in = new FileInputStream(certificateName)){
            return certificateFactory.generateCertificate(in);
        }
    }

    private static KeyStore loadTrustAnchor(String certificateName) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        try(FileInputStream in = new FileInputStream(certificateName)){
            KeyStore trustAnchor = KeyStore.getInstance("jks");
            trustAnchor.load(in, PASSWORD);
            return trustAnchor;
        }
    }

    private static void decipher(String fileName, String resultFile, String metadataFile) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, CertificateException, KeyStoreException, UnrecoverableKeyException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        AsymmetricDecipher asymDecipher = new AsymmetricDecipher();
        PrivateKey key = getPrivateKey(asymDecipher.get(AsymmetricDecipher.KEYSTORE));
        asymDecipher.init(key);
        Pair<SecretKey, byte[]> pair = asymDecipher.execute(metadataFile);

        SymmetricDecipher symDecipher = new SymmetricDecipher();
        symDecipher.init(pair.getKey(), pair.getValue());
        symDecipher.execute(fileName, resultFile);
    }

    private static PrivateKey getPrivateKey(String pfxFileName) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        try(FileInputStream in = new FileInputStream(pfxFileName)) {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(in, PASSWORD);
            return (PrivateKey) ks.getKey("1", PASSWORD);
        }
    }
}