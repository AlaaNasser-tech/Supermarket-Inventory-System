package ui;

import java.awt.*;
import javax.swing.*;
import service.AdminService;
import service.ClientService;

public class MainDashboard extends JFrame {
    private AdminService adminService;
    private ClientService clientService;

    public MainDashboard() {

        // 1. Initialize Services
        // Create one shared instance so all screens use the same data
        adminService = new AdminService();
        clientService = new ClientService(adminService);

        // 2. Main Window Settings
        setTitle("Inventory Management System");
        setSize(500, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Open in center of screen
        setLayout(new GridLayout(4, 1, 15, 15)); // Space between buttons

        // 3. Create Buttons
        JButton adminBtn = new JButton("Admin Module (Categories & Suppliers)");
        JButton productBtn = new JButton("Product Module (Inventory)");
        JButton clientBtn = new JButton("Client Module (Orders & Sales)");
        JButton exitBtn = new JButton("Exit System");

        // 4. Font Styling
        Font font = new Font("Arial", Font.BOLD, 16);

        adminBtn.setFont(font);
        productBtn.setFont(font);
        clientBtn.setFont(font);
        exitBtn.setFont(font);

        exitBtn.setForeground(Color.RED); // Highlight Exit button

        // 5. Button Actions

        // Open Admin Screen
        adminBtn.addActionListener(e ->
                new AdminFrame(adminService).setVisible(true)
        );

        // Open Product Screen
        productBtn.addActionListener(e ->
                new ProductFrame(adminService).setVisible(true)
        );

        // Open Client Screen
        clientBtn.addActionListener(e -> {
            ClientFrame cf = new ClientFrame(adminService, clientService);
            cf.setVisible(true);
        });

        // Exit Button
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // 6. Add Buttons to Window
        add(adminBtn);
        add(productBtn);
        add(clientBtn);
        add(exitBtn);
    }
}