import java.util.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Interface
interface Manageable {
    void display();
}

// Abstract User Class
abstract class User implements Manageable {
    private String id;
    private String username;
    private String password;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public abstract String getType();

    @Override
    public void display() {
        System.out.println("ID: " + id);
        System.out.println("Username: " + username);
        System.out.println("Type: " + getType());
    }

    public String toFile() {
        return id + "," + username + "," + password + "," + getType();
    }
}

// Customer Class
class Customer extends User {
    private ArrayList<String> orders;

    public Customer(String id, String username, String password) {
        super(id, username, password);
        this.orders = new ArrayList<>();
    }

    @Override
    public String getType() {
        return "CUSTOMER";
    }

    public void addOrder(String orderId) {
        orders.add(orderId);
        System.out.println("Order added!");
    }

    public void showOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders yet.");
        } else {
            System.out.println("Orders: " + orders);
        }
    }
}

// Admin Class
class Admin extends User {
    public Admin(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getType() {
        return "ADMIN";
    }
}

// Product Class
class Product implements Manageable {
    private String id;
    private String name;
    private double price;
    private int stock;

    public Product(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void reduceStock(int qty) {
        if (qty <= stock) {
            stock -= qty;
        }
    }

    public void addStock(int qty) {
        stock += qty;
    }

    @Override
    public void display() {
        System.out.println(id + " | " + name + " | $" + price + " | Stock: " + stock);
    }

    public String toFile() {
        return id + "," + name + "," + price + "," + stock;
    }
}

// CartItem Class
class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }

    public void display() {
        System.out.println(product.getName() + " x" + quantity + " = $" + getTotal());
    }
}

// ShoppingCart Class
class ShoppingCart {
    private ArrayList<CartItem> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public boolean addItem(Product product, int qty) {
        if (qty > product.getStock()) {
            System.out.println("Not enough stock!");
            return false;
        }
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                System.out.println("Already in cart!");
                return false;
            }
        }
        items.add(new CartItem(product, qty));
        System.out.println("Added to cart!");
        return true;
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        System.out.println("Removed!");
    }

    public void display() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }
        System.out.println("\n--- CART ---");
        for (CartItem item : items) {
            item.display();
        }
        System.out.println("Total: $" + getTotal());
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getTotal).sum();
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }
}

// Transaction Class
class Transaction {
    private String username;
    private double amount;
    private Date date;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Transaction(String username, double amount, Date date) {
        this.username = username;
        this.amount = amount;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String toString() {
        return "| " + username + " | $" + String.format("%.2f", amount) + " | " + sdf.format(date) + " |";
    }
}

class EcommerceSystem {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ShoppingCart cart = new ShoppingCart();
    private User currentUser;

    void loadData() {
        File userFile = new File("users.txt");
        if (userFile.exists()) {
            try (Scanner sc = new Scanner(userFile)) {
                while (sc.hasNextLine()) {
                    String[] p = sc.nextLine().split(",");
                    users.add(p[3].equals("CUSTOMER") ? new Customer(p[0], p[1], p[2]) : new Admin(p[0], p[1], p[2]));
                }
            } catch (Exception e) {
                System.out.println("User file issue: " + e.getMessage());
            }
        }

        File productFile = new File("products.txt");
        if (productFile.exists()) {
            try (Scanner sc = new Scanner(productFile)) {
                while (sc.hasNextLine()) {
                    String[] p = sc.nextLine().split(",");
                    products.add(new Product(p[0], p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3])));
                }
            } catch (Exception e) {
                System.out.println("Product file issue: " + e.getMessage());
            }
        }

        File txnFile = new File("transactions.txt");
        if (txnFile.exists()) {
            try (Scanner sc = new Scanner(txnFile)) {
                while (sc.hasNextLine()) {
                    String[] p = sc.nextLine().split("\\|\\|");
                    if (p.length == 3)
                        transactions.add(new Transaction(p[0].trim(), Double.parseDouble(p[1].trim()),
                                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(p[2].trim())));
                }
            } catch (Exception e) {
                System.out.println("Transaction file issue: " + e.getMessage());
            }
        }

        if (users.isEmpty())
            users.add(new Admin("A1", "admin", "admin"));
        if (products.isEmpty()) {
            products.add(new Product("P1", "Laptop", 999, 5));
            products.add(new Product("P2", "Mouse", 25, 10));
        }
    }

    void saveData() {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("users.txt"));
            users.forEach(u -> pw.println(u.toFile()));
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("products.txt"));
            products.forEach(p -> pw.println(p.toFile()));
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
        try {
            PrintWriter pw = new PrintWriter(new FileWriter("transactions.txt"));
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Transaction t : transactions)
                pw.println(t.getUsername() + "||" + t.getAmount() + "||" + sdf.format(t.getDate()));
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    public User login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                currentUser = u;
                cart.clear();
                return u;
            }
        }
        return null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
        cart.clear();
    }

    public boolean register(String username, String password) {
        for (User u : users)
            if (u.getUsername().equals(username))
                return false;
        String id = "C" + (users.size() + 1);
        users.add(new Customer(id, username, password));
        return true;
    }

    void showProducts() {
        System.out.println("\n--- PRODUCTS ---");
        if (products.isEmpty())
            System.out.println("No products available.");
        else
            products.forEach(p -> p.display());
    }

    Product findProduct(String id) {
        return products.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    boolean addProductToCart(String id, int qty) {
        Product p = findProduct(id);
        if (p == null || qty <= 0)
            return false;
        return cart.addItem(p, qty);
    }

    void viewCart() {
        cart.display();
    }

    ShoppingCart getCart() {
        return cart;
    }

    List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    List<User> getUsers() {
        return new ArrayList<>(users);
    }

    List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    void showUsers() {
        System.out.println("\n--- USERS ---");
        users.forEach(u -> System.out
                .println(u.getId() + " | " + u.getUsername() + " | " + u.getClass().getSimpleName().toUpperCase()));
    }

    boolean removeUser(String username) {
        Iterator<User> it = users.iterator();
        while (it.hasNext()) {
            User u = it.next();
            if (u.getUsername().equals(username)) {
                if (u instanceof Admin)
                    return false;
                it.remove();
                return true;
            }
        }
        return false;
    }

    boolean addProduct(String id, String name, double price, int stock) {
        if (findProduct(id) != null)
            return false;
        products.add(new Product(id, name, price, stock));
        return true;
    }

    boolean removeProduct(String id) {
        return products.removeIf(p -> p.getId().equals(id));
    }

    boolean processPayment(double t, Scanner sc) {
        if (t <= 0)
            return false;
        System.out.println("\n--- PAYMENT OPTIONS ---\n1. Credit Card\n2. Debit Card\n3. Digital Wallet");
        System.out.print("Select: ");
        try {
            int choice = sc.nextInt();
            sc.nextLine();
            if (choice < 1 || choice > 3)
                return false;
        } catch (Exception e) {
            sc.nextLine();
            return false;
        }
        if (!applyCartStockDeduction()) {
            System.out.println("Unable to checkout: one or more items are out of stock.");
            return false;
        }
        System.out.println("Payment of $" + String.format("%.2f", t)
                + " processed successfully!\nOrder confirmed. Thank you for your purchase!");
        recordTransaction(currentUser.getUsername(), t);
        cart.clear();
        return true;
    }

    boolean processPayment(double t) {
        if (t <= 0 || currentUser == null)
            return false;
        if (!applyCartStockDeduction())
            return false;
        recordTransaction(currentUser.getUsername(), t);
        cart.clear();
        return true;
    }

    double getCartTotal() {
        return cart.getTotal();
    }

    void recordTransaction(String username, double amount) {
        transactions.add(new Transaction(username, amount, new java.util.Date()));
    }

    private boolean applyCartStockDeduction() {
        if (cart.getItems().stream().anyMatch(item -> item.getQuantity() > item.getProduct().getStock()))
            return false;
        cart.getItems().forEach(item -> item.getProduct().reduceStock(item.getQuantity()));
        return true;
    }

    void showPaymentHistory() {
        if (transactions.isEmpty()) {
            System.out.println("\n--- PAYMENT HISTORY ---\nNo transactions recorded.");
            return;
        }
        System.out.println("\n--- PAYMENT HISTORY ---");
        transactions.forEach(System.out::println);
        double totalRevenue = transactions.stream().mapToDouble(Transaction::getAmount).sum();
        System.out.println("\nTotal Revenue: $" + String.format("%.2f", totalRevenue));
    }

    void showUserPurchaseHistory(String username) {
        var userTrans = transactions.stream().filter(t -> t.getUsername().equals(username)).toList();
        if (userTrans.isEmpty()) {
            System.out.println("\nNo purchases by " + username);
            return;
        }
        System.out.println("\n--- " + username.toUpperCase() + "'S PURCHASE HISTORY ---");
        userTrans.forEach(System.out::println);
        double total = userTrans.stream().mapToDouble(Transaction::getAmount).sum();
        System.out.println("Total Spent: $" + String.format("%.2f", total));
    }
}

public class ECommerceApp {
    private static int getValidChoice(Scanner sc) {
        try {
            int choice = sc.nextInt();
            sc.nextLine();
            return choice;
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a number.");
            sc.nextLine();
            return -1;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> new ECommerceGUI().setVisible(true));
    }

    static void adminMenu(Scanner sc, EcommerceSystem sys) {
        int invalidAttempts = 0;
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println(
                    "1. View Products\n2. Add Product\n3. Remove Product\n4. View Users\n5. Remove User\n6. View Payment History\n7. View User Purchase History\n8. Logout");
            System.out.print("Choice: ");
            int choice = ECommerceApp.getValidChoice(sc);
            if (choice == -1) {
                invalidAttempts++;
                if (invalidAttempts >= 3) {
                    System.out.println("\nWARNING: Too many invalid attempts. Logging out for security.");
                    break;
                }
                continue;
            }
            invalidAttempts = 0;
            if (choice == 1)
                sys.showProducts();
            else if (choice == 2) {
                System.out.print("Product ID: ");
                String id = sc.nextLine();
                System.out.print("Product Name: ");
                String name = sc.nextLine();
                try {
                    System.out.print("Price: ");
                    double price = sc.nextDouble();
                    System.out.print("Stock: ");
                    int stock = sc.nextInt();
                    sc.nextLine();
                    if (sys.addProduct(id, name, price, stock))
                        System.out.println("Product added successfully!");
                    else
                        System.out.println("Product ID already exists!");
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                    sc.nextLine();
                }
            } else if (choice == 3) {
                System.out.print("Product ID to remove: ");
                if (sys.removeProduct(sc.nextLine()))
                    System.out.println("Product removed successfully!");
                else
                    System.out.println("Product not found!");
            } else if (choice == 4)
                sys.showUsers();
            else if (choice == 5) {
                System.out.print("Username to remove: ");
                if (sys.removeUser(sc.nextLine()))
                    System.out.println("User removed successfully!");
                else
                    System.out.println("User not found or cannot remove admin!");
            } else if (choice == 6)
                sys.showPaymentHistory();
            else if (choice == 7) {
                System.out.print("Username to check: ");
                sys.showUserPurchaseHistory(sc.nextLine());
            } else if (choice == 8)
                break;
            else {
                System.out.println("Invalid choice! Please select 1-8.");
                invalidAttempts++;
                if (invalidAttempts >= 3) {
                    System.out.println("\nWARNING: Too many invalid attempts. Logging out for security.");
                    break;
                }
            }
        }
    }

    static void customerMenu(Scanner sc, EcommerceSystem sys) {
        int invalidAttempts = 0;
        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1. View Products\n2. Add to Cart\n3. View Cart\n4. Checkout\n5. Logout");
            System.out.print("Choice: ");
            int choice = ECommerceApp.getValidChoice(sc);
            if (choice == -1) {
                invalidAttempts++;
                if (invalidAttempts >= 3) {
                    System.out.println("\nWARNING: Too many invalid attempts. Logging out for security.");
                    break;
                }
                continue;
            }
            invalidAttempts = 0;
            if (choice == 1)
                sys.showProducts();
            else if (choice == 2) {
                System.out.print("Product ID: ");
                String id = sc.nextLine();
                System.out.print("Quantity: ");
                try {
                    int qty = sc.nextInt();
                    sc.nextLine();
                    if (sys.addProductToCart(id, qty))
                        System.out.println("Added to cart!");
                    else
                        System.out.println("Product not found or insufficient stock!");
                } catch (Exception e) {
                    System.out.println("Invalid quantity!");
                    sc.nextLine();
                }
            } else if (choice == 3)
                sys.viewCart();
            else if (choice == 4) {
                double total = sys.getCartTotal();
                if (total <= 0) {
                    System.out.println("Cart is empty!");
                } else {
                    boolean paid = sys.processPayment(total, sc);
                    if (paid)
                        System.out.println("Transaction completed!");
                    else
                        System.out.println("Checkout failed. Please review your cart and try again.");
                }
            } else if (choice == 5)
                break;
            else {
                System.out.println("Invalid choice! Please select 1-5.");
                invalidAttempts++;
                if (invalidAttempts >= 3) {
                    System.out.println("\nWARNING: Too many invalid attempts. Logging out for security.");
                    break;
                }
            }
        }
    }
}
