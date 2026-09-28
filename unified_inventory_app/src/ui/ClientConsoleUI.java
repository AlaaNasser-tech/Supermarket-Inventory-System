package ui;

import model.Client;
import model.Order;
import service.AdminService;
import service.ClientService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ClientConsoleUI {
    private final AdminService adminService;
    private final ClientService clientService;
    private final Scanner scanner;

    public ClientConsoleUI(AdminService adminService, ClientService clientService, Scanner scanner) {
        this.adminService = adminService;
        this.clientService = clientService;
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
        System.out.println("\n================ CLIENT MODULE ================");
        System.out.println("1. Register Client");
        System.out.println("2. Edit Client Data");
        System.out.println("3. Request Purchase Order");
        System.out.println("4. Generate Orders Report");
        System.out.println("5. List Clients");
        System.out.println("0. Back");
        System.out.println("===============================================");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 1:
                registerClient();
                break;
            case 2:
                editClient();
                break;
            case 3:
                requestOrder();
                break;
            case 4:
                generateOrdersReport();
                break;
            case 5:
                listClients();
                break;
            case 0:
                System.out.println("Returning to main menu...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void registerClient() {
        int id = readInt("Client ID: ");
        String name = readLine("Client Name: ");
        String email = readLine("Email: ");
        String phone = readLine("Phone: ");
        String address = readLine("Address: ");
        boolean added = clientService.registerClient(new Client(id, name, email, phone, address));
        System.out.println(added ? "Client registered successfully." : "Client ID already exists.");
    }

    private void editClient() {
        int id = readInt("Client ID: ");
        String name = readLine("New Name: ");
        String phone = readLine("New Phone: ");
        String address = readLine("New Address: ");
        boolean updated = clientService.updateClient(id, name, phone, address);
        System.out.println(updated ? "Client updated successfully." : "Client not found.");
    }

    private void requestOrder() {
        if (adminService.getAllProducts().isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        System.out.println("Available products:");
        for (model.Product product : adminService.getAllProducts()) {
            System.out.println("ID: " + product.getId() + " | Name: " + product.getName() + " | Qty: " + product.getQuantity() + " | Price: " + clientService.getActiveProductPrice(product.getId()));
        }

        int orderId = readInt("Order ID: ");
        int clientId = readInt("Client ID: ");
        int productId = readInt("Product ID: ");
        int quantity = readInt("Quantity: ");
        Optional<Order> order = clientService.requestOrder(orderId, clientId, productId, quantity);
        if (!order.isPresent()) {
            System.out.println("Failed to create order. Check client/product IDs or available quantity.");
            return;
        }
        System.out.println("Order requested successfully.");
        System.out.println(clientService.generateInvoice(order.get()));
        System.out.println(clientService.buildEmailNotification(order.get()));
    }

    private void generateOrdersReport() {
        int clientId = readInt("Enter client ID to show orders (0 for all): ");
        List<Order> orders = clientId == 0 ? clientService.getAllOrders() : clientService.getOrdersByClientId(clientId);
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    private void listClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }
        for (Client client : clients) {
            System.out.println(client);
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

    private String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }
}
