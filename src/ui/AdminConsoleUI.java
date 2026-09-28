package ui;
import model.Category;
import model.Offer;
import model.Supplier;
import service.AdminService;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AdminConsoleUI {
    private final AdminService adminService;
    private final Scanner scanner;

    public AdminConsoleUI(AdminService adminService, Scanner scanner) {
        this.adminService = adminService;
        this.scanner = scanner;
    }

    public AdminService getAdminService() {
        return adminService;
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
        System.out.println("\n================ ADMIN MODULE ================");
        System.out.println("1. Add Category");
        System.out.println("2. Update Category");
        System.out.println("3. Delete Category");
        System.out.println("4. List Categories");
        System.out.println("5. Add Supplier");
        System.out.println("6. Update Supplier");
        System.out.println("7. Delete Supplier");
        System.out.println("8. List Suppliers");
        System.out.println("9. Add Offer");
        System.out.println("10. List Offers");
        System.out.println("11. Generate Products Report");
        System.out.println("12. Generate Category Statistics");
        System.out.println("13. Generate Profit Report");
        System.out.println("0. Back");
        System.out.println("==============================================");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                addCategory();
                break;
            case 2:
                updateCategory();
                break;
            case 3:
                deleteCategory();
                break;
            case 4:
                listCategories();
                break;
            case 5:
                addSupplier();
                break;
            case 6:
                updateSupplier();
                break;
            case 7:
                deleteSupplier();
                break;
            case 8:
                listSuppliers();
                break;
            case 9:
                addOffer();
                break;
            case 10:
                listOffers();
                break;
            case 11:
                System.out.println(adminService.generateProductsReport());
                break;
            case 12:
                System.out.println(adminService.generateCategoryStatistics());
                break;
            case 13:
                System.out.println(adminService.generateProfitReport());
                break;
            case 0:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void addCategory() {
        int id = readInt("Category ID: ");
        String name = readLine("Category Name: ");
        String description = readLine("Description: ");
        boolean added = adminService.addCategory(new Category(id, name, description));
        System.out.println(added ? "Category added successfully." : "Category ID already exists.");
    }

    private void updateCategory() {
        int id = readInt("Enter category ID to update: ");
        String name = readLine("New Name: ");
        String description = readLine("New Description: ");
        boolean updated = adminService.updateCategory(id, name, description);
        System.out.println(updated ? "Category updated successfully." : "Category not found.");
    }

    private void deleteCategory() {
        int id = readInt("Enter category ID to delete: ");
        boolean deleted = adminService.deleteCategory(id);
        System.out.println(deleted ? "Category deleted successfully." : "Category not found or linked to products.");
    }
private void listCategories() {
        List<Category> categories = adminService.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    private void addSupplier() {
        int id = readInt("Supplier ID: ");
        String name = readLine("Supplier Name: ");
        String phone = readLine("Phone: ");
        String email = readLine("Email: ");
        String address = readLine("Address: ");
        boolean added = adminService.addSupplier(new Supplier(id, name, phone, email, address));
        System.out.println(added ? "Supplier added successfully." : "Supplier ID already exists.");
    }

    private void updateSupplier() {
        int id = readInt("Enter supplier ID to update: ");
        String name = readLine("New Name: ");
        String phone = readLine("New Phone: ");
        String email = readLine("New Email: ");
        String address = readLine("New Address: ");
        boolean updated = adminService.updateSupplier(id, name, phone, email, address);
        System.out.println(updated ? "Supplier updated successfully." : "Supplier not found.");
    }

    private void deleteSupplier() {
        int id = readInt("Enter supplier ID to delete: ");
        boolean deleted = adminService.deleteSupplier(id);
        System.out.println(deleted ? "Supplier deleted successfully." : "Supplier not found or linked to products.");
    }

    private void listSuppliers() {
        List<Supplier> suppliers = adminService.getAllSuppliers();
        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return;
        }
        for (Supplier supplier : suppliers) {
            System.out.println(supplier);
        }
    }

    private void addOffer() {
        int id = readInt("Offer ID: ");
        int productId = readInt("Product ID: ");
        double discount = readDouble("Discount Percentage: ");
        LocalDate startDate = readDate("Start Date (yyyy-mm-dd): ");
        LocalDate endDate = readDate("End Date (yyyy-mm-dd): ");
        Offer offer = new Offer(id, productId, discount, startDate, endDate);
        boolean added = adminService.addOffer(offer);
        System.out.println(added ? "Offer added successfully." : "Failed to add offer. Check product ID or dates or duplicate offer ID.");
    }

    private void listOffers() {
        List<Offer> offers = adminService.getAllOffers();
        if (offers.isEmpty()) {
            System.out.println("No offers found.");
            return;
        }
        for (Offer offer : offers) {
            System.out.println(offer);
        }
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