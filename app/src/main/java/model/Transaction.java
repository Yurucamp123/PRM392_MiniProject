package model;
public class Transaction {
    private String type; // "NẠP TIỀN", "RÚT TIỀN"
    private double amount;
    private String dateTime;

    public Transaction(String type, double amount, String dateTime) {
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDateTime() { return dateTime; }
}

