package com.nit;
import java.util.*;

public class ECommerceApp {
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static List<Product> products = new ArrayList<>();
    private static Map<String, List<Product>> carts = new HashMap<>();

    public static void main(String[] args) {
        initializeProducts();
        System.out.println("Welcome to the E-Commerce App!");

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> {
                    System.out.println("Thank you for using the E-Commerce App!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Initialize some default products
    private static void initializeProducts() {
        products.add(new Product(1, "Laptop", "High-performance laptop", 50000, 10));
        products.add(new Product(2, "Phone", "Latest smartphone", 30000, 15));
        products.add(new Product(3, "Headphones", "Noise-cancelling headphones", 2000, 20));
    }

    // User Registration
    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        users.add(new User(username, password));
        System.out.println("Registration successful!");
    }

    // User Login
    private static void loginUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();

        if (user.isPresent()) {
            System.out.println("Login successful! Welcome, " + username + "!");
            userMenu(username);
        } else {
            System.out.println("Invalid credentials. Try again.");
        }
    }

    // User Menu
    private static void userMenu(String username) {
        while (true) {
            System.out.println("\n1. View Products\n2. Add to Cart\n3. View Cart\n4. Checkout\n5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewProducts();
                case 2 -> addToCart(username);
                case 3 -> viewCart(username);
                case 4 -> checkout(username);
                case 5 -> {
                    System.out.println("Logged out successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // View Products
    private static void viewProducts() {
        System.out.println("\nAvailable Products:");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    // Add Product to Cart
    private static void addToCart(String username) {
        System.out.print("Enter Product ID to add to cart: ");
        int productId = scanner.nextInt();

        Product product = products.stream()
                .filter(p -> p.getId() == productId && p.getStock() > 0)
                .findFirst()
                .orElse(null);

        if (product != null) {
            carts.putIfAbsent(username, new ArrayList<>());
            carts.get(username).add(product);
            product.setStock(product.getStock() - 1);
            System.out.println("Product added to cart!");
        } else {
            System.out.println("Product not found or out of stock.");
        }
    }

    // View Cart
    private static void viewCart(String username) {
        List<Product> cart = carts.get(username);
        if (cart == null || cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        System.out.println("\nYour Cart:");
        double total = 0;
        for (Product product : cart) {
            System.out.println(product);
            total += product.getPrice();
        }
        System.out.println("Total: ₹" + total);
    }

    // Checkout
    private static void checkout(String username) {
        List<Product> cart = carts.get(username);
        if (cart == null || cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }

        double total = cart.stream().mapToDouble(Product::getPrice).sum();
        System.out.println("Total Amount: ₹" + total);
        System.out.println("Order placed successfully! Thank you for shopping.");
        carts.remove(username); // Clear the cart
    }
}

// User Class
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

// Product Class
class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;

    public Product(int id, String name, String description, double price, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return id + ". " + name + " - ₹" + price + " (Stock: " + stock + ")";
    }
}
