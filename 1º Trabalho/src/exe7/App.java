package exe7;

import exe7.criptographicBlocks.AsymmetricCipher;
import exe7.criptographicBlocks.AsymmetricDecipher;
import exe7.criptographicBlocks.SymmetricCipher;
import exe7.criptographicBlocks.SymmetricDecipher;
import javafx.util.Pair;
import javax.crypto.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class App {
    private static String resultFile, metadataFile;
    private static final char[] PASSWORD = "changeit".toCharArray();

    public static void main(String[] args) {
        long initialTime = System.currentTimeMillis();
        String fileName = args[0], operation = args[1];
        try {
            String[] components = fileName.split("[.]");

            if(operation.equalsIgnoreCase("cipher")) {
                resultFile = String.format("%sCiphered.%s", components[0], components[1]);
                metadataFile = String.format("%sMetaData.%s", components[0], components[1]);
                cipherMode(fileName);
            }
            else if(operation.equalsIgnoreCase("decipher")) {
                resultFile = String.format("%sDeciphered.%s", components[0], components[1]);
                metadataFile = components[0].replace("Ciphered", "") + "Metadata." + components[1];
                decipherMode(fileName);
            }
            else
                System.out.println("Invalid Operation.\n Valid operations are \"cipher\" and \"decipher\"");

            System.out.println(String.format("Time elapsed in millis: %d", System.currentTimeMillis() - initialTime));
        } catch (InvalidKeyException | CertPathValidatorException | CertificateException | NoSuchAlgorithmException
                | KeyStoreException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | UnrecoverableKeyException | IOException | BadPaddingException | NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (CertPathBuilderException e) {
            System.out.println("Invalid trust anchor, configure a diferent one in ASYMCipherConfiguration.txt");
        }


    }

    private static void cipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, CertificateException, KeyStoreException, CertPathValidatorException, CertPathBuilderException {
        SymmetricCipher symCipher = new SymmetricCipher();
        SecretKey secretKey = generateKey(symCipher.getConfiguration().get(SymmetricCipher.PRIMITIVE));
        symCipher.init(secretKey);

        symCipher.execute(fileName, resultFile);

        AsymmetricCipher asymCipher = new AsymmetricCipher();

        HashMap<String, String> asymCipherConfig = asymCipher.getConfiguration();

        asymCipher.init(GetPublicKey(asymCipherConfig.get(AsymmetricCipher.CERTIFICATE), asymCipherConfig.get(AsymmetricCipher.TRUST_ANCHOR)));

        asymCipher.execute(secretKey, symCipher.getIV(), metadataFile);
    }

    private static SecretKey generateKey(String primitive) throws NoSuchAlgorithmException {
        return KeyGenerator.getInstance(primitive).generateKey();
    }

    private static PublicKey GetPublicKey(String certificate, String trustAnchorName) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CertPathBuilderException, CertPathValidatorException {
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

    /**
     * It's assumed the package only contains certificates
     * @param certificateFactory
     * @param leaf
     * @return
     * @throws CertificateException
     * @throws IOException
     */
    private static Collection<Certificate> getIntermediateAndLeafCertificates(CertificateFactory certificateFactory, X509Certificate leaf) throws CertificateException, IOException {
        File folder = new File("cert.CAintermedia");
        File[] listOfFiles = folder.listFiles();
        ArrayList<Certificate> certificates = new ArrayList<>();

        certificates.add(leaf);
        for (File listOfFile : listOfFiles)
            certificates.add(loadCertificate(certificateFactory, listOfFile.getPath()));

        return certificates;
    }

    private static Certificate loadCertificate(CertificateFactory certificateFactory, String certificateName ) throws IOException, CertificateException {
        Certificate cert;
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(certificateName))){
            cert = certificateFactory.generateCertificate(bis);
        }
        return cert;
    }

    private static KeyStore loadTrustAnchor(String certificateName) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        try(FileInputStream fis = new FileInputStream(certificateName)){
            KeyStore trustAnchor1 = KeyStore.getInstance("jks");
            trustAnchor1.load(fis, PASSWORD);
            return trustAnchor1;
        }
    }

    private static void decipherMode(String fileName) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, KeyStoreException, CertificateException, UnrecoverableKeyException {
        AsymmetricDecipher asymDecipher = new AsymmetricDecipher();

        HashMap<String, String> asymDecipherConfig = asymDecipher.getConfiguration();

        PrivateKey key = getPrivateKey(asymDecipherConfig.get(AsymmetricDecipher.KEYSTORE));

        asymDecipher.init(key);
        Pair<SecretKey, byte[]> pair = asymDecipher.execute(metadataFile);

        SymmetricDecipher symDecipher = new SymmetricDecipher();

        symDecipher.init(pair.getKey(), pair.getValue());
        symDecipher.execute(fileName, resultFile);
    }

    private static PrivateKey getPrivateKey(String pfxFileName) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        FileInputStream fis = new java.io.FileInputStream(pfxFileName);
        ks.load(fis, PASSWORD);
        fis.close();
        return (PrivateKey)ks.getKey("1", PASSWORD);
    }
}