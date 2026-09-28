package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Category;
import service.AdminService;

public class AdminFrame extends JFrame {
    private AdminService adminService;
    private JTable table;
    private DefaultTableModel model;

    public AdminFrame(AdminService adminService) {
        this.adminService = adminService;

        setTitle("Admin Module - Categories");
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Table
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Description"},
                0
        );

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons Panel
        JPanel btnPanel = new JPanel();

        JButton addCatBtn = new JButton("Add Category");
        JButton listReportBtn = new JButton("Profit Report");

        btnPanel.add(addCatBtn);
        btnPanel.add(listReportBtn);

        add(btnPanel, BorderLayout.SOUTH);

        // Actions

        // Add Category Button
        addCatBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Category ID:");
            String name = JOptionPane.showInputDialog("Enter Category Name:");
            String desc = JOptionPane.showInputDialog("Enter Description:");

            if (idStr != null && name != null) {
                adminService.addCategory(
                        new Category(
                                Integer.parseInt(idStr),
                                name,
                                desc
                        )
                );

                refreshTable();
            }
        });

        // Profit Report Button
        listReportBtn.addActionListener(e -> {
            String report = adminService.generateProfitReport();

            JOptionPane.showMessageDialog(
                    this,
                    new JTextArea(report)
            );
        });

        refreshTable();
    }

    private void refreshTable() {
        model.setRowCount(0);

        for (Category c : adminService.getAllCategories()) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getDescription()
            });
        }
    }
}