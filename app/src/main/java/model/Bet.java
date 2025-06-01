package model;
public class Bet {
    private int raceId;
    private double amount;
    private String result;

    public Bet(int raceId, double amount, String result) {
        this.raceId = raceId;
        this.amount = amount;
        this.result = result;
    }

    public int getRaceId() { return raceId; }
    public double getAmount() { return amount; }
    public String getResult() { return result; }
}

