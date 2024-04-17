package blockchain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Block implements Serializable {


    private final int DIFFICULTY = 2; // from config
    private int index;
    private long timestamp;
    private List<Transaction> transactions;
    private String previousHash;
    private String hash;
    private int nonce; // For proof-of-work

    public Block(int index, long timestamp, List<Transaction> transactionList, String previousHash){
        this.index = index;
        this.timestamp = timestamp;
        this.transactions = transactionList;
        this.previousHash = previousHash;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String data = index + timestamp + transactions.toString() + previousHash + nonce;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isValid(Block previousBlock){
        if (!hash.equals(calculateHash())) {
            return false;
        }

        String target = new String(new char[DIFFICULTY]).replace('\0', '0'); // Set the target hash with the required number of leading zeroes
        if (!hash.substring(0, DIFFICULTY).equals(target)) {
            return false;
        }

        if (!previousHash.equals(previousBlock.getHash())) {
            return false;
        }

        return true;
    }

    public void mineBlock() {
        String target = new String(new char[DIFFICULTY]).replace('\0', '0'); // Set the target hash with the required number of leading zeroes
        while (!hash.substring(0, DIFFICULTY).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }


    public String getHash(){
        return hash;
    }

}
