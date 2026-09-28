package ui;

import service.AdminService;
import service.ClientService;

import java.time.LocalDate;
import java.util.Scanner;

public class ApplicationConsoleUI {
    private final AdminConsoleUI adminConsoleUI;
    private final ProductConsoleUI productConsoleUI;
    private final ClientConsoleUI clientConsoleUI;
    private final Scanner scanner;

    public ApplicationConsoleUI(AdminService adminService, ClientService clientService) {
        this.scanner = new Scanner(System.in);
        this.adminConsoleUI = new AdminConsoleUI(adminService, scanner);
        this.productConsoleUI = new ProductConsoleUI(adminService, scanner);
        this.clientConsoleUI = new ClientConsoleUI(adminService, clientService, scanner);
    }

    public void start() {
        seedSampleDataIfNeeded();
        int choice;
        do {
            printMenu();
            choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1:
                    adminConsoleUI.start();
                    break;
                case 2:
                    productConsoleUI.start();
                    break;
                case 3:
                    clientConsoleUI.start();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 0);
    }

    private void printMenu() {
        System.out.println("\n============= INVENTORY MANAGEMENT SYSTEM =============");
        System.out.println("1. Admin Module");
        System.out.println("2. Product Module");
        System.out.println("3. Client Module");
        System.out.println("0. Exit");
        System.out.println("=======================================================");
    }

    private void seedSampleDataIfNeeded() {
        if (adminConsoleUI.getAdminService().getAllCategories().isEmpty()) {
            adminConsoleUI.getAdminService().addCategory(new model.Category(1, "Beverages", "Drinks and juices"));
            adminConsoleUI.getAdminService().addCategory(new model.Category(2, "Dairy", "Milk, cheese, yogurt"));
        }

        if (adminConsoleUI.getAdminService().getAllSuppliers().isEmpty()) {
            adminConsoleUI.getAdminService().addSupplier(new model.Supplier(1, "Juhayna", "01000000000", "sales@juhayna.com", "Cairo"));
            adminConsoleUI.getAdminService().addSupplier(new model.Supplier(2, "Edita", "01111111111", "contact@edita.com", "Giza"));
        }

        if (adminConsoleUI.getAdminService().getAllProducts().isEmpty()) {
            adminConsoleUI.getAdminService().addProduct(new model.Product(
                    1, "Orange Juice", 1, 1,
                    18.0, 25.0, 50,
                    LocalDate.now().minusDays(10),
                    LocalDate.now().plusDays(25)
            ));

            adminConsoleUI.getAdminService().addProduct(new model.Product(
                    2, "Milk 1L", 2, 1,
                    20.0, 28.0, 5,
                    LocalDate.now().minusDays(2),
                    LocalDate.now().plusDays(8)
            ));
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
}
