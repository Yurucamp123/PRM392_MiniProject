package model;

public class Bet {
    private int raceId;
    private double amount;
    private String result;      // e.g., "Win +100,000 VND" or "Lose -50,000 VND"
    private String dateTime;    // e.g., "01/06/2025 14:00"
    private String winningCar;  // e.g., "Xe số 5"

    public Bet(int raceId, double amount, String result, String dateTime, String winningCar) {
        this.raceId = raceId;
        this.amount = amount;
        this.result = result;
        this.dateTime = dateTime;
        this.winningCar = winningCar;
    }

    // Getters
    public int getRaceId() { return raceId; }
    public double getAmount() { return amount; }
    public String getResult() { return result; }
    public String getDateTime() { return dateTime; }
    public String getWinningCar() { return winningCar; }

    // Optional: Setters if needed
    public void setRaceId(int raceId) { this.raceId = raceId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setResult(String result) { this.result = result; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setWinningCar(String winningCar) { this.winningCar = winningCar; }
}
