package ui;

import model.Product;
import service.AdminService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ProductConsoleUI {
    private final AdminService adminService;
    private final Scanner scanner;

    public ProductConsoleUI(AdminService adminService, Scanner scanner) {
        this.adminService = adminService;
        this.scanner = scanner;
    }

    public void start() {
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            handleChoice(choice);
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n================ PRODUCT MODULE ================");
        System.out.println("1. Add Product");
        System.out.println("2. Show Products");
        System.out.println("3. Search Product");
        System.out.println("4. Update Product");
        System.out.println("5. Delete Product");
        System.out.println("6. Low Stock Report");
        System.out.println("7. Near Expiry Report");
        System.out.println("0. Back");
        System.out.println("===============================================");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                showProducts();
                break;
            case 3:
                searchProduct();
                break;
            case 4:
                updateProduct();
                break;
            case 5:
                deleteProduct();
                break;
            case 6:
                lowStockReport();
                break;
            case 7:
                nearExpiryReport();
                break;
            case 0:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addProduct() {
        int id = readInt("Product ID: ");
        String name = readLine("Product Name: ");
        int categoryId = readInt("Category ID: ");
        int supplierId = readInt("Supplier ID: ");
        double costPrice = readDouble("Cost Price: ");
        double sellingPrice = readDouble("Selling Price: ");
        int quantity = readInt("Quantity: ");
        LocalDate productionDate = readDate("Production Date (yyyy-mm-dd): ");
        LocalDate expirationDate = readDate("Expiration Date (yyyy-mm-dd): ");
        Product product = new Product(id, name, categoryId, supplierId, costPrice, sellingPrice, quantity, productionDate, expirationDate);
        boolean added = adminService.addProduct(product);
        System.out.println(added ? "Product added successfully." : "Failed to add product. Check category/supplier IDs or duplicate product ID.");
    }

    private void showProducts() {
        List<Product> products = adminService.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private void searchProduct() {
        System.out.println("Search by:");
        System.out.println("1. Name");
        System.out.println("2. Category ID");
        System.out.println("3. Expiration Date");
        System.out.println("4. Production Date");
        int choice = readInt("Choose: ");
        List<Product> results;
        switch (choice) {
            case 1:
                results = adminService.searchProductsByName(readLine("Enter name: "));
                break;
            case 2:
                results = adminService.searchProductsByCategoryId(readInt("Enter category ID: "));
                break;
            case 3:
                results = adminService.searchProductsByExpirationDate(readDate("Enter expiration date (yyyy-mm-dd): "));
                break;
            case 4:
                results = adminService.searchProductsByProductionDate(readDate("Enter production date (yyyy-mm-dd): "));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (results.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product product : results) {
            System.out.println(product);
        }
    }

    private void updateProduct() {
        int id = readInt("Enter product ID to update: ");
        String name = readLine("New Name: ");
        int categoryId = readInt("New Category ID: ");
        int supplierId = readInt("New Supplier ID: ");
        double costPrice = readDouble("New Cost Price: ");
        double sellingPrice = readDouble("New Selling Price: ");
        int quantity = readInt("New Quantity: ");
        LocalDate productionDate = readDate("New Production Date (yyyy-mm-dd): ");
        LocalDate expirationDate = readDate("New Expiration Date (yyyy-mm-dd): ");
        boolean updated = adminService.updateProduct(id, name, categoryId, supplierId, costPrice, sellingPrice, quantity, productionDate, expirationDate);
        System.out.println(updated ? "Product updated successfully." : "Product not found or invalid category/supplier IDs.");
    }

    private void deleteProduct() {
        int id = readInt("Enter product ID to delete: ");
        boolean deleted = adminService.deleteProduct(id);
        System.out.println(deleted ? "Product deleted successfully." : "Product not found.");
    }

    private void lowStockReport() {
        int threshold = readInt("Enter low stock threshold: ");
        System.out.println(adminService.generateLowStockReport(threshold));
    }

    private void nearExpiryReport() {
        int days = readInt("Enter days threshold: ");
        System.out.println(adminService.generateNearExpiryReport(days));
    }

    private int readInt(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private LocalDate readDate(String message) {
        while (true) {
            try {
                System.out.print(message);
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter the date in format yyyy-mm-dd.");
            }
        }
    }

    private String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}
