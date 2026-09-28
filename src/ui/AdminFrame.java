package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Category;
import model.Supplier;
import service.AdminService;

public class AdminFrame extends JFrame {

    private AdminService adminService;

    private JTable categoryTable;
    private DefaultTableModel categoryModel;

    private JTable supplierTable;
    private DefaultTableModel supplierModel;

    public AdminFrame(AdminService adminService) {
        this.adminService = adminService;

        setTitle("Admin Module");
        setSize(800, 500);
        setLayout(new BorderLayout());

        // ================= CATEGORY TABLE =================
        categoryModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Description"}, 0
        );
        categoryTable = new JTable(categoryModel);

        // ================= SUPPLIER TABLE =================
        supplierModel = new DefaultTableModel(
                new String[]{"ID", "Name", "Phone", "Email", "Address"}, 0
        );
        supplierTable = new JTable(supplierModel);

        // ================= TABS =================
        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Categories", new JScrollPane(categoryTable));
        tabs.add("Suppliers", new JScrollPane(supplierTable));

        add(tabs, BorderLayout.CENTER);

        // ================= BUTTONS =================
        JPanel btnPanel = new JPanel();

        JButton addCatBtn = new JButton("Add Category");
        JButton addSupBtn = new JButton("Add Supplier");
        JButton reportBtn = new JButton("Profit Report");

        btnPanel.add(addCatBtn);
        btnPanel.add(addSupBtn);
        btnPanel.add(reportBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // ================= ADD CATEGORY =================
        addCatBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Category ID:");
            String name = JOptionPane.showInputDialog("Enter Category Name:");
            String desc = JOptionPane.showInputDialog("Enter Description:");

            adminService.addCategory(
                    new Category(Integer.parseInt(idStr), name, desc)
            );

            refreshCategories();
        });

        // ================= ADD SUPPLIER =================
        addSupBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Supplier ID:");
            String name = JOptionPane.showInputDialog("Enter Supplier Name:");
            String phone = JOptionPane.showInputDialog("Enter Phone:");
            String email = JOptionPane.showInputDialog("Enter Email:");
            String address = JOptionPane.showInputDialog("Enter Address:");

            adminService.addSupplier(
                    new Supplier(
                            Integer.parseInt(idStr),
                            name,
                            phone,
                            email,
                            address
                    )
            );

            JOptionPane.showMessageDialog(this, "Supplier added successfully!");

            refreshSuppliers();
        });

        // ================= REPORT =================
        reportBtn.addActionListener(e -> {
            String report = adminService.generateProfitReport();
            JOptionPane.showMessageDialog(this, new JTextArea(report));
        });

        // أول تحميل
        refreshCategories();
        refreshSuppliers();
    }

    // ================= REFRESH CATEGORY =================
    private void refreshCategories() {
        categoryModel.setRowCount(0);

        for (Category c : adminService.getAllCategories()) {
            categoryModel.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getDescription()
            });
        }
    }
    // ================= REFRESH SUPPLIER =================
    private void refreshSuppliers() {
        supplierModel.setRowCount(0);
        for (Supplier s : adminService.getAllSuppliers()) {
            supplierModel.addRow(new Object[]{
                    s.getId(),
                    s.getName(),
                    s.getPhone(),
                    s.getEmail(),
                    s.getAddress()
            });
        }
    }
}