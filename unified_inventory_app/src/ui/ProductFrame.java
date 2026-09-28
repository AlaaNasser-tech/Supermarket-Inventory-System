package ui;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Product;
import service.AdminService;

public class ProductFrame extends JFrame {
    private AdminService adminService;
    private JTable table;
    private DefaultTableModel tableModel;

    // Input fields
    private JTextField idF, nameF, catIdF, supIdF, costF, sellF, qtyF, prodDateF, expDateF;

    public ProductFrame(AdminService adminService) {
        this.adminService = adminService;

        setTitle("Product Management - Product Module");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Product Information"));

        inputPanel.add(new JLabel("Product ID:"));
        idF = new JTextField();
        inputPanel.add(idF);

        inputPanel.add(new JLabel("Name:"));
        nameF = new JTextField();
        inputPanel.add(nameF);

        inputPanel.add(new JLabel("Category ID:"));
        catIdF = new JTextField();
        inputPanel.add(catIdF);

        inputPanel.add(new JLabel("Supplier ID:"));
        supIdF = new JTextField();
        inputPanel.add(supIdF);

        inputPanel.add(new JLabel("Cost Price:"));
        costF = new JTextField();
        inputPanel.add(costF);

        inputPanel.add(new JLabel("Selling Price:"));
        sellF = new JTextField();
        inputPanel.add(sellF);

        inputPanel.add(new JLabel("Quantity:"));
        qtyF = new JTextField();
        inputPanel.add(qtyF);

        inputPanel.add(new JLabel("Production Date (yyyy-mm-dd):"));
        prodDateF = new JTextField(LocalDate.now().toString());
        inputPanel.add(prodDateF);

        inputPanel.add(new JLabel("Expiration Date:"));
        expDateF = new JTextField();
        inputPanel.add(expDateF);

        // 2. Product Table
        String[] columns = {
                "ID", "Name", "Category", "Supplier",
                "Cost Price", "Selling Price", "Quantity", "Expiration Date"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // 3. Action Buttons Panel
        JPanel actionPanel = new JPanel();

        JButton addBtn = new JButton("Add Product");
        JButton searchBtn = new JButton("Search by Name");
        JButton lowStockBtn = new JButton("Low Stock Report");
        JButton refreshBtn = new JButton("Refresh All");

        actionPanel.add(addBtn);
        actionPanel.add(searchBtn);
        actionPanel.add(lowStockBtn);
        actionPanel.add(refreshBtn);

        // Build UI
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // --- Events ---

        // Add Product Button
        addBtn.addActionListener(e -> {
            try {
                Product p = new Product(
                        Integer.parseInt(idF.getText()),
                        nameF.getText(),
                        Integer.parseInt(catIdF.getText()),
                        Integer.parseInt(supIdF.getText()),
                        Double.parseDouble(costF.getText()),
                        Double.parseDouble(sellF.getText()),
                        Integer.parseInt(qtyF.getText()),
                        LocalDate.parse(prodDateF.getText()),
                        LocalDate.parse(expDateF.getText())
                );

                if (adminService.addProduct(p)) {
                    JOptionPane.showMessageDialog(this, "Product added successfully!");
                    refreshTable(adminService.getAllProducts());
                } else {
                    JOptionPane.showMessageDialog(this, "Add failed! Please check the IDs.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Data Error: " + ex.getMessage());
            }
        });

        // Search Button
        searchBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter product name to search:");

            if (name != null) {
                refreshTable(adminService.searchProductsByName(name));
            }
        });

        // Low Stock Report Button
        lowStockBtn.addActionListener(e -> {
            String threshold = JOptionPane.showInputDialog(
                    "Enter quantity threshold:",
                    "5"
            );

            if (threshold != null) {
                String report = adminService.generateLowStockReport(
                        Integer.parseInt(threshold)
                );

                JOptionPane.showMessageDialog(
                        this,
                        new JTextArea(report)
                );
            }
        });

        // Refresh Button
        refreshBtn.addActionListener(e ->
                refreshTable(adminService.getAllProducts())
        );

        refreshTable(adminService.getAllProducts());
    }

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
    }
}