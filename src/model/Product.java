package model;

import java.time.LocalDate;

public class Product {
    private int id;
    private String name;
    private int categoryId;
    private int supplierId;
    private double costPrice;
    private double sellingPrice;
    private int quantity;
    private LocalDate productionDate;
    private LocalDate expirationDate;

    public Product() {
    }

    public Product(int id, String name, int categoryId, int supplierId, double costPrice,
                   double sellingPrice, int quantity, LocalDate productionDate, LocalDate expirationDate) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.supplierId = supplierId;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(LocalDate productionDate) {
        this.productionDate = productionDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isLowStock(int threshold) {
        return quantity <= threshold;
    }

    public String toFileString() {
        return id + "," + safe(name) + "," + categoryId + "," + supplierId + "," +
                costPrice + "," + sellingPrice + "," + quantity + "," + productionDate + "," + expirationDate;
    }

    public static Product fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 9) {
            throw new IllegalArgumentException("Invalid product record: " + line);
        }
        return new Product(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(),
                Integer.parseInt(parts[2].trim()),
                Integer.parseInt(parts[3].trim()),
                Double.parseDouble(parts[4].trim()),
                Double.parseDouble(parts[5].trim()),
                Integer.parseInt(parts[6].trim()),
                LocalDate.parse(parts[7].trim()),
                LocalDate.parse(parts[8].trim())
        );
    }

    private String safe(String value) {
        return value == null ? "" : value.replace(",", " ");
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", supplierId=" + supplierId +
                ", costPrice=" + costPrice +
                ", sellingPrice=" + sellingPrice +
                ", quantity=" + quantity +
                ", productionDate=" + productionDate +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
