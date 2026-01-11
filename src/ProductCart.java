import java.util.*;

// 1. Product Class
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

// 2. CartItem Class
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

// 3. ShoppingCart Class
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
        if (items.stream().anyMatch(item -> item.getProduct().getId().equals(product.getId()))) {
            System.out.println("Already in cart!");
            return false;
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
        items.forEach(CartItem::display);
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

// 4. Test Only For Member 2
class TestCart {
    public static void main(String[] args) {
        Product p1 = new Product("P1", "Laptop", 999, 5);
        Product p2 = new Product("P2", "Mouse", 25, 10);

        ShoppingCart cart = new ShoppingCart();

        cart.addItem(p1, 1);
        cart.addItem(p2, 2);

        cart.display();
    }
}
