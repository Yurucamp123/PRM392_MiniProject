package model;

public class Bet {
    private int raceId;
    private double amount;
    private String result;      // e.g., "THẮNG +100,000 VND" or "THUA -50,000 VND"
    private String dateTime;    // e.g., "01/06/2025 14:00"
    private String winningCar;  // e.g., "Xe đua đỏ"
    private String selectedCar; // e.g., "Xe đua đen" - xe mà người chơi đã chọn
    private double odds;        // Tỷ lệ cược (ví dụ: 2.5)

    public Bet(int raceId, double amount, String result, String dateTime, String winningCar) {
        this.raceId = raceId;
        this.amount = amount;
        this.result = result;
        this.dateTime = dateTime;
        this.winningCar = winningCar;
        this.selectedCar = "";
        this.odds = 2.5; // Default odds
    }

    // Enhanced constructor
    public Bet(int raceId, double amount, String result, String dateTime, String winningCar, String selectedCar, double odds) {
        this.raceId = raceId;
        this.amount = amount;
        this.result = result;
        this.dateTime = dateTime;
        this.winningCar = winningCar;
        this.selectedCar = selectedCar;
        this.odds = odds;
    }

    // Getters
    public int getRaceId() { return raceId; }
    public double getAmount() { return amount; }
    public String getResult() { return result; }
    public String getDateTime() { return dateTime; }
    public String getWinningCar() { return winningCar; }
    public String getSelectedCar() { return selectedCar; }
    public double getOdds() { return odds; }

    // Setters
    public void setRaceId(int raceId) { this.raceId = raceId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setResult(String result) { this.result = result; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public void setWinningCar(String winningCar) { this.winningCar = winningCar; }
    public void setSelectedCar(String selectedCar) { this.selectedCar = selectedCar; }
    public void setOdds(double odds) { this.odds = odds; }

    // Utility methods
    public boolean isWin() {
        return result.contains("THẮNG");
    }

    public String getBetStatus() {
        if (isWin()) {
            return "WIN";
        } else {
            return "LOSE";
        }
    }

    public String getDisplayResult() {
        return isWin() ? "🎉 THẮNG" : "😔 THUA";
    }
}