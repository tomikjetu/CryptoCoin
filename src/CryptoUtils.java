import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.*;
import java.util.Base64;

public class CryptoUtils {
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    private static byte[] hexToBytes(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }

    public static PrivateKey generatePrivateKey() {
        try {

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();
            return keyPair.getPrivate();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPrivateAddress(PrivateKey key){
        return bytesToHex(key.getEncoded());
    }

    public static String getPublicAddress(PublicKey key){
        return bytesToHex(key.getEncoded());
    }

    public static PrivateKey hexToPrivateKey(String hexPrivateKey) {
        try {

            byte[] privateKeyBytes = hexToBytes(hexPrivateKey);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey hexToPublicKey(String hexPublicKey){
        try {
            byte[] privateKeyBytes = hexToBytes(hexPublicKey);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateKeyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return keyFactory.generatePublic(keySpec);
        }         catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey derivePublicKey(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) privateKey;

        final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateCrtKey.getModulus(),
                rsaPrivateCrtKey.getPublicExponent());

        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicKeySpec);
    }

    public static byte[] sign(byte[] data, PrivateKey privateKey) {
        try {

            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initSign(privateKey);

            signature.update(data);

            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(byte[] data, byte[] signature, PublicKey publicKey) {
        try {

            Signature verifier = Signature.getInstance("SHA256withRSA");

            verifier.initVerify(publicKey);

            verifier.update(data);

            return verifier.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

}