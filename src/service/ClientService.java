package service;

import model.Client;
import model.Order;
import model.Product;
import ui.util.FileUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientService {
    private static final String CLIENT_FILE = "clients.txt";
    private static final String ORDER_FILE = "orders.txt";

    private final AdminService adminService;
    private final List<Client> clients;
    private final List<Order> orders;

    public ClientService(AdminService adminService) {
        this.adminService = adminService;
        this.clients = loadClients();
        this.orders = loadOrders();
    }

    public boolean registerClient(Client client) {
        if (findClientById(client.getId()).isPresent()) {
            return false;
        }
        clients.add(client);
        saveClients();
        return true;
    }

    public boolean updateClient(int clientId, String newName, String newPhone, String newAddress) {
        Optional<Client> optionalClient = findClientById(clientId);
        if (!optionalClient.isPresent()) {
            return false;
        }
        Client client = optionalClient.get();
        client.setName(newName);
        client.setPhone(newPhone);
        client.setAddress(newAddress);
        saveClients();
        return true;
    }

    public Optional<Client> findClientById(int clientId) {
        for (Client client : clients) {
            if (client.getId() == clientId) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    public List<Client> getAllClients() {
        return new ArrayList<Client>(clients);
    }

    public Optional<Order> requestOrder(int orderId, int clientId, int productId, int quantity) {
        Optional<Client> optionalClient = findClientById(clientId);
        Optional<Product> optionalProduct = adminService.findProductById(productId);

        if (!optionalClient.isPresent() || !optionalProduct.isPresent()) {
            return Optional.empty();
        }
        if (quantity <= 0) {
            return Optional.empty();
        }

        Product product = optionalProduct.get();
        if (product.getQuantity() < quantity) {
            return Optional.empty();
        }

        double unitPrice = getActiveProductPrice(productId);
        double totalPrice = unitPrice * quantity;
        Order order = new Order(
                orderId,
                clientId,
                productId,
                product.getName(),
                quantity,
                unitPrice,
                totalPrice,
                LocalDate.now()
        );

        if (!adminService.reduceProductQuantity(productId, quantity)) {
            return Optional.empty();
        }

        orders.add(order);
        saveOrders();
        return Optional.of(order);
    }

    public double getActiveProductPrice(int productId) {
        Optional<Product> optionalProduct = adminService.findProductById(productId);
        if (!optionalProduct.isPresent()) {
            return 0;
        }
        Product product = optionalProduct.get();
        double sellingPrice = product.getSellingPrice();
        LocalDate today = LocalDate.now();
        for (model.Offer offer : adminService.getAllOffers()) {
            if (offer.getProductId() == productId && offer.isActive(today)) {
                double discountValue = sellingPrice * (offer.getDiscountPercentage() / 100.0);
                return sellingPrice - discountValue;
            }
        }
        return sellingPrice;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<Order>(orders);
    }

    public List<Order> getOrdersByClientId(int clientId) {
        List<Order> result = new ArrayList<Order>();
        for (Order order : orders) {
            if (order.getClientId() == clientId) {
                result.add(order);
            }
        }
        return result;
    }

    public String generateInvoice(Order order) {
        StringBuilder invoice = new StringBuilder();
        Optional<Client> optionalClient = findClientById(order.getClientId());
        invoice.append("\n----- Invoice -----\n");
        invoice.append("Order ID: ").append(order.getOrderId()).append("\n");
        if (optionalClient.isPresent()) {
            invoice.append("Client Name: ").append(optionalClient.get().getName()).append("\n");
        }
        invoice.append("Product: ").append(order.getProductName()).append("\n");
        invoice.append("Quantity: ").append(order.getQuantity()).append("\n");
        invoice.append("Unit Price: ").append(String.format("%.2f", order.getUnitPrice())).append("\n");
        invoice.append("Total Price: ").append(String.format("%.2f", order.getTotalPrice())).append("\n");
        invoice.append("Order Date: ").append(order.getOrderDate()).append("\n");
        invoice.append("-------------------\n");
        return invoice.toString();
    }

    public String buildEmailNotification(Order order) {
        Optional<Client> optionalClient = findClientById(order.getClientId());
        if (!optionalClient.isPresent()) {
            return "Client not found.";
        }
        return "Email sent to " + optionalClient.get().getEmail() + " for order #" + order.getOrderId();
    }

    private List<Client> loadClients() {
        List<Client> list = new ArrayList<Client>();
        List<String> lines = FileUtil.readLines(CLIENT_FILE);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                list.add(Client.fromFileString(line));
            }
        }
        return list;
    }

    private void saveClients() {
        List<String> lines = new ArrayList<String>();
        for (Client client : clients) {
            lines.add(client.toFileString());
        }
        FileUtil.writeLines(CLIENT_FILE, lines);
    }

    private List<Order> loadOrders() {
        List<Order> list = new ArrayList<Order>();
        List<String> lines = FileUtil.readLines(ORDER_FILE);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                list.add(Order.fromFileString(line));
            }
        }
        return list;
    }

    private void saveOrders() {
        List<String> lines = new ArrayList<String>();
        for (Order order : orders) {
            lines.add(order.toFileString());
        }
        FileUtil.writeLines(ORDER_FILE, lines);
    }
}
