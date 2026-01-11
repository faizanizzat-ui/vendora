import java.util.Date;
import java.text.SimpleDateFormat;

class Transaction {
  private String username;
  private double amount;
  private Date date;
  private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  Transaction(String username, double amount, Date date) {
    this.username = username;
    this.amount = amount;
    this.date = date;
  }

  String getUsername() {
    return username;
  }

  double getAmount() {
    return amount;
  }

  Date getDate() {
    return date;
  }

  public String toString() {
    return "| " + username + " | $" + String.format("%.2f", amount) + " | " + sdf.format(date) + " |";
  }
}
