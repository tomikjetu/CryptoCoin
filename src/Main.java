import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class Main {
    static final int PORT = 4073; // put to config

    public static void CreatePeerServer(){
        Server server = new Server(PORT);
    }

    public static void ConnectToPeer(String address){
        Client client = new Client(address, PORT);
    }

    public static void CreateAddress(){
        try{
            PrivateKey privateKey = CryptoUtils.generatePrivateKey();
            String privateAddress = CryptoUtils.getPrivateAddress(privateKey);
            PublicKey publicKey = CryptoUtils.derivePublicKey(privateKey);
            String publicAddress = CryptoUtils.getPublicAddress(publicKey);

            System.out.println("Na tuto adresu si nechaj posielat peniaze: " + publicAddress);
            System.out.println("Tytmto sa prihlasuj do systemu: " + privateAddress);
        }catch (Exception e){
            System.out.println("Sorry");
        }
    }

    public static byte[] SignData(byte[] data, String privateAddress){
        PrivateKey privateKey = CryptoUtils.hexToPrivateKey(privateAddress);
        byte[] signature = CryptoUtils.sign(data, privateKey);
        return signature;
    }

    public static boolean VerifyDataSignature(byte[] data, byte[] signature, String publicAddress){
        PublicKey publicKey = CryptoUtils.hexToPublicKey(publicAddress);
        boolean verified = CryptoUtils.verify(data, signature, publicKey);
        return verified;
    }


    public static void main(String[] args) {
       //String clientAddress = args[0];




    }
}
