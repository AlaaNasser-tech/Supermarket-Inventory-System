package ui;

import model.Product;
import service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ProductFrame extends JFrame {

    private AdminService adminService;

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<String> searchType;

    public ProductFrame(AdminService adminService) {

        this.adminService = adminService;

        setTitle("Product Module");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= TABLE =================
        tableModel = new DefaultTableModel(
                new String[]{
                        "ID", "Name", "Category",
                        "Supplier", "Cost", "Sell",
                        "Qty", "Expiry"
                }, 0
        );

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ================= TOP PANEL =================
        JPanel panel = new JPanel();

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        JButton lowStockBtn = new JButton("Low Stock");
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(deleteBtn);
        panel.add(searchBtn);
        panel.add(refreshBtn);
        panel.add(lowStockBtn);

        add(panel, BorderLayout.SOUTH);

        // ================= ADD =================
        addBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("ID"));
                String name = JOptionPane.showInputDialog("Name");
                int cat = Integer.parseInt(JOptionPane.showInputDialog("Category ID"));
                int sup = Integer.parseInt(JOptionPane.showInputDialog("Supplier ID"));
                double cost = Double.parseDouble(JOptionPane.showInputDialog("Cost"));
                double sell = Double.parseDouble(JOptionPane.showInputDialog("Sell"));
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Qty"));
                LocalDate prod = LocalDate.parse(JOptionPane.showInputDialog("Production Date yyyy-mm-dd"));
                LocalDate exp = LocalDate.parse(JOptionPane.showInputDialog("Expiration Date yyyy-mm-dd"));

                adminService.addProduct(
                        new Product(id, name, cat, sup, cost, sell, qty, prod, exp)
                );

                refreshTable(adminService.getAllProducts());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Data");
            }
        });

        // ================= UPDATE =================
        updateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Product ID"));
                String name = JOptionPane.showInputDialog("New Name");
                int cat = Integer.parseInt(JOptionPane.showInputDialog("Category ID"));
                int sup = Integer.parseInt(JOptionPane.showInputDialog("Supplier ID"));
                double cost = Double.parseDouble(JOptionPane.showInputDialog("Cost"));
                double sell = Double.parseDouble(JOptionPane.showInputDialog("Sell"));
                int qty = Integer.parseInt(JOptionPane.showInputDialog("Qty"));
                LocalDate prod = LocalDate.parse(JOptionPane.showInputDialog("Production Date yyyy-mm-dd"));
                LocalDate exp = LocalDate.parse(JOptionPane.showInputDialog("Expiration Date yyyy-mm-dd"));
                adminService.updateProduct(id, name, cat, sup, cost, sell, qty, prod, exp);

                refreshTable(adminService.getAllProducts());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update Error");
            }
        });

        // ================= DELETE =================
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Product ID"));

                adminService.deleteProduct(id);

                refreshTable(adminService.getAllProducts());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Delete Error");
            }
        });

        // ================= SEARCH (FIXED) =================
        searchBtn.addActionListener(e -> {

    try {

        String[] options = {
                "Name",
                "ID",
                "Category ID",
                "Production Date",
                "Expiration Date"
        };

        String type = (String) JOptionPane.showInputDialog(
                this,
                "Search By:",
                "Product Search",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (type == null) return;

        String value = JOptionPane.showInputDialog("Enter value:");
        if (value == null || value.trim().isEmpty()) return;

        List<Product> results = new java.util.ArrayList<>();

        for (Product p : adminService.getAllProducts()) {

            switch (type) {

                case "Name":
                    if (p.getName().toLowerCase().contains(value.toLowerCase()))
                        results.add(p);
                    break;

                case "ID":
                    if (p.getId() == Integer.parseInt(value))
                        results.add(p);
                    break;

                case "Category ID":
                    if (p.getCategoryId() == Integer.parseInt(value))
                        results.add(p);
                    break;

                case "Production Date":
                    if (p.getProductionDate().toString().equals(value))
                        results.add(p);
                    break;

                case "Expiration Date":
                    if (p.getExpirationDate().toString().equals(value))
                        results.add(p);
                    break;
            }
        }

        refreshTable(results);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this,
                "Invalid input format!");
    }
});
        // ================= REFRESH =================
        refreshBtn.addActionListener(e ->
                refreshTable(adminService.getAllProducts())
        );

        // ================= LOW STOCK =================
        lowStockBtn.addActionListener(e -> {
            try {
                int t = Integer.parseInt(
                        JOptionPane.showInputDialog("Threshold")
                );

                JOptionPane.showMessageDialog(
                        this,
                        adminService.generateLowStockReport(t)
                );

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error");
            }
        });

        // أول تحميل
        refreshTable(adminService.getAllProducts());
    }

    // ================= TABLE + NOTIFICATIONS =================
    private void refreshTable(List<Product> products) {

        tableModel.setRowCount(0);

        for (Product p : products) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getCategoryId(),
                    p.getSupplierId(),
                    p.getCostPrice(),
                    p.getSellingPrice(),
                    p.getQuantity(),
                    p.getExpirationDate()
            });
        }

        checkNotifications(products);
    }

    // ================= NOTIFICATIONS =================
    private void checkNotifications(List<Product> products) {

        StringBuilder msg = new StringBuilder();
        LocalDate today = LocalDate.now();

        for (Product p : products) {

            if (p.getExpirationDate() != null &&
                    p.getExpirationDate().isBefore(today)) {

                msg.append("❌ EXPIRED: ").append(p.getName()).append("\n");
            }
            else if (p.getExpirationDate() != null &&
                    p.getExpirationDate().isBefore(today.plusDays(7))) {

                msg.append("⚠️ Near Expiry: ").append(p.getName()).append("\n");
            }

            if (p.getQuantity() <= 5) {
                msg.append(" Low Stock: ")
                        .append(p.getName())
                        .append(" (")
                        .append(p.getQuantity())
                        .append(")\n");
            }
        }

        if (msg.length() > 0) {
            JOptionPane.showMessageDialog(this, msg.toString());
        }
    }
}