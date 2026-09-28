package model;

import java.time.LocalDate;

public class Offer {
    private int id;
    private int productId;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public Offer() {
    }

    public Offer(int id, int productId, double discountPercentage, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.productId = productId;
        this.discountPercentage = discountPercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive(LocalDate today) {
        return (today.isEqual(startDate) || today.isAfter(startDate)) &&
               (today.isEqual(endDate) || today.isBefore(endDate));
    }

    public String toFileString() {
        return id + "," + productId + "," + discountPercentage + "," + startDate + "," + endDate;
    }

    public static Offer fromFileString(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Invalid offer record: " + line);
        }
        return new Offer(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Double.parseDouble(parts[2].trim()),
                LocalDate.parse(parts[3].trim()),
                LocalDate.parse(parts[4].trim())
        );
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", productId=" + productId +
                ", discountPercentage=" + discountPercentage +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
