package model;

import java.time.LocalDate;

public class Order {
    private int orderId;
    private int clientId;
    private int productId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDate orderDate;

    public Order() {
    }

    public Order(int orderId, int clientId, int productId, String productName, int quantity,
                 double unitPrice, double totalPrice, LocalDate orderDate) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String toFileString() {
        return orderId + "," + clientId + "," + productId + "," + clean(productName) + "," + quantity + "," +
                unitPrice + "," + totalPrice + "," + orderDate;
    }

    public static Order fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid order record: " + line);
        }
        return new Order(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim()),
                parts[3].trim(),
                Integer.parseInt(parts[4].trim()),
                Double.parseDouble(parts[5].trim()),
                Double.parseDouble(parts[6].trim()),
                LocalDate.parse(parts[7].trim())
        );
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", clientId=" + clientId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                '}';
    }
}
