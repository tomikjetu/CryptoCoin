package blockchain;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String sender;
    private String recipient;
    private double amount;

    public Transaction(String sender, String recipient, double amount){
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public double getAmount() {
        return amount;
    }
}
