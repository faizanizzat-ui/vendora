import java.util.*;

// 1. Interface
interface Manageable {
    void display();
}

// 2. Abstract User Class
abstract class User implements Manageable {
    private String id;
    private String username;
    private String password;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

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

// 3. Customer Class
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
        System.out.println(orders.isEmpty() ? "No orders yet." : "Orders: " + orders);
    }
}

// 4. Admin Class
class Admin extends User {
    public Admin(String id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getType() {
        return "ADMIN";
    }
}

// 5. Test Only For Member 1
class TestUser {
    public static void main(String[] args) {

        Customer c = new Customer("C1", "john", "123");
        Admin a = new Admin("A1", "admin", "admin");

        c.display();
        a.display();

        c.addOrder("ORD001");
        c.showOrders();
    }
}
