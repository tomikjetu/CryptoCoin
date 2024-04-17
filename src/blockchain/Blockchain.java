package blockchain;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private final String FILENAME = "blockchain.dat"; // load from config
    private boolean isLoaded = false;
    private boolean isVerified = false;

    private List<Block> chain;
    private List<Transaction> currentTransactions;

    public Blockchain(){
        loadFromFile();
        if(isLoaded) {
            System.out.println("Loaded length of " + chain.size());
            return;
        }
        createGenesisBlock();
    }

    public boolean validateBlockChain(){
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Check if the current block is valid
            if (!currentBlock.isValid(previousBlock)) {
                System.out.println("Invalid block at index " + i);
                isVerified=false;
                return false;
            }
        }
        isVerified = true;
        return true;
    }

    public void createGenesisBlock(){
        chain = new ArrayList<>();
        System.out.println("Creating genesis block");
        List<Transaction> genesisTransactions = new ArrayList<>();
        Block genesisBlock = new Block(1, System.currentTimeMillis(), genesisTransactions, "0");
        chain.add(genesisBlock);

        saveToFile();
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(chain);
            oos.writeObject(currentTransactions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("File does not exist: " + FILENAME);
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            chain = (List<Block>) ois.readObject();
            currentTransactions = (List<Transaction>) ois.readObject();
            isLoaded = true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
