import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

// Product Class
class Product {
    private String name;
    private double price;
    private int stock;
    private boolean onSale;

    public Product(String name, double price, int stock, boolean onSale) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.onSale = onSale;
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

    public boolean isOnSale() {
        return onSale;
    }

    public void applyDiscount(double discountPercentage) {
        if (!onSale) {
            price -= price * (discountPercentage / 100);
        } else {
            System.out.println("Discount cannot be applied to " + name + " as it is on sale.");
        }
    }

    public void reduceStock(int quantity) {
        if (stock >= quantity) {
            stock -= quantity;
        } else {
            System.out.println("Not enough stock for " + name);
        }
    }
}

// Cart Class
class Cart {
    private Map<Product, Integer> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItem(Product product, int quantity) {
        if (product.getStock() >= quantity) {
            items.put(product, items.getOrDefault(product, 0) + quantity);
            product.reduceStock(quantity);
        } else {
            System.out.println("Insufficient stock for " + product.getName());
        }
    }

    public double calculateTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }

    public void clearCart() {
        items.clear();
    }

    public Map<Product, Integer> getItems() {
        return items;
    }
}

// User Class
class User {
    private String name;
    private String email;
    private Cart cart;
    private boolean loggedIn;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.cart = new Cart();
        this.loggedIn = false;
    }

    public void login() {
        loggedIn = true;
        System.out.println(name + " logged in.");
    }

    public void logout() {
        loggedIn = false;
        System.out.println(name + " logged out.");
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void addToCart(Product product, int quantity) {
        if (loggedIn) {
            cart.addItem(product, quantity);
        } else {
            System.out.println("You must be logged in to add items to the cart.");
        }
    }

    public Cart getCart() {
        return cart;
    }
}

// Order Class
class Order {
    private User user;
    private Cart cart;
    private LocalDateTime orderTime;
    private boolean isCanceled;

    public Order(User user) {
        if (!user.isLoggedIn()) {
            throw new IllegalStateException("User must be logged in to place an order.");
        }
        this.user = user;
        this.cart = user.getCart();
        this.orderTime = LocalDateTime.now();
        this.isCanceled = false;
    }

    public boolean cancelOrder() {
        long hoursSinceOrder = ChronoUnit.HOURS.between(orderTime, LocalDateTime.now());
        if (hoursSinceOrder <= 24) {
            isCanceled = true;
            cart.clearCart();
            System.out.println("Order canceled successfully.");
            return true;
        } else {
            System.out.println("Order cannot be canceled after 24 hours.");
            return false;
        }
    }

    public double getTotalAmount() {
        return cart.calculateTotal();
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}

// Main Class
public class OnlineShoppingSystem {
    public static void main(String[] args) {
        // Create products
        Product laptop = new Product("Laptop", 65999.0, 10, false);
        Product Tshirt = new Product("Tshirt", 1999.0, 5, true);

        // Create a user
        User user = new User("koustubh", "koustubhrayamane2001@gmail.com");

        // User must log in
        user.login();

        // Add products to cart
        user.addToCart(laptop, 1);
        user.addToCart(Tshirt, 1); // Tshirt is on sale, so no discount applies

        // Apply discount (only to non-sale items)
        laptop.applyDiscount(10);  // Laptop price should be 900
        Tshirt.applyDiscount(10);   // Discount not applied as it's on sale

        // Place an order
        Order order = new Order(user);
        System.out.println("Total Order Cost: " + order.getTotalAmount());

        // Attempt to cancel within 24 hours
        order.cancelOrder();
    }
}