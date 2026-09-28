package ui;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Client;
import model.Order;
import service.AdminService;
import service.ClientService;

public class ClientFrame extends JFrame {
    private final AdminService adminService;
    private final ClientService clientService;
    private JTable clientTable;
    private DefaultTableModel tableModel;

    // Client input fields
    private JTextField idF, nameF, emailF, phoneF, addressF;

    public ClientFrame(AdminService adminService, ClientService clientService) {
        this.adminService = adminService;
        this.clientService = clientService;

        setTitle("Client and Sales Module");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. New Client Registration Panel
        JPanel registerPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        registerPanel.setBorder(BorderFactory.createTitledBorder("Register New Client"));

        registerPanel.add(new JLabel("Client ID:")); 
        idF = new JTextField(); 
        registerPanel.add(idF);

        registerPanel.add(new JLabel("Name:")); 
        nameF = new JTextField(); 
        registerPanel.add(nameF);

        registerPanel.add(new JLabel("Email:")); 
        emailF = new JTextField(); 
        registerPanel.add(emailF);

        registerPanel.add(new JLabel("Phone:")); 
        phoneF = new JTextField(); 
        registerPanel.add(phoneF);

        registerPanel.add(new JLabel("Address:")); 
        addressF = new JTextField(); 
        registerPanel.add(addressF);

        JButton regBtn = new JButton("Register Client");
        registerPanel.add(new JLabel("")); // Empty space for formatting
        registerPanel.add(regBtn);

        // 2. Client Display Table
        String[] columns = {"ID", "Name", "Email", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0);
        clientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(clientTable);

        // 3. Action Panel (New Order / Reports)
        JPanel actionPanel = new JPanel();
        JButton orderBtn = new JButton("New Purchase Order");
        JButton reportBtn = new JButton("View Client Orders");
        JButton refreshBtn = new JButton("Refresh List");

        actionPanel.add(orderBtn);
        actionPanel.add(reportBtn);
        actionPanel.add(refreshBtn);

        // Build UI
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(registerPanel, BorderLayout.WEST);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // --- Events ---

        // Register Client
        regBtn.addActionListener(e -> {
            try {
                Client c = new Client(
                    Integer.parseInt(idF.getText()),
                    nameF.getText(),
                    emailF.getText(),
                    phoneF.getText(),
                    addressF.getText()
                );

                if (clientService.registerClient(c)) {
                    JOptionPane.showMessageDialog(this, "Client registered successfully!");
                    refreshClientTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Client ID already exists.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid data.");
            }
        });

        // New Purchase Order
        orderBtn.addActionListener(e -> {
            try {
                String oId = JOptionPane.showInputDialog("Order ID:");
                String cId = JOptionPane.showInputDialog("Client ID:");
                String pId = JOptionPane.showInputDialog("Product ID:");
                String qty = JOptionPane.showInputDialog("Quantity:");

                if (oId != null && cId != null && pId != null && qty != null) {
                    Optional<Order> order = clientService.requestOrder(
                        Integer.parseInt(oId),
                        Integer.parseInt(cId),
                        Integer.parseInt(pId),
                        Integer.parseInt(qty)
                    );

                    if (order.isPresent()) {
                        String invoice = clientService.generateInvoice(order.get());
                        String email = clientService.buildEmailNotification(order.get());

                        JTextArea area = new JTextArea(
                            "Order completed successfully!\n\n" +
                            invoice +
                            "\n\n" +
                            email
                        );

                        area.setEditable(false);

                        JOptionPane.showMessageDialog(
                            this,
                            new JScrollPane(area),
                            "Invoice and Notification",
                            JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {
                        JOptionPane.showMessageDialog(
                            this,
                            "Order failed! Please check IDs and available quantity."
                        );
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input error.");
            }
        });

        // View Client Orders
        reportBtn.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Enter Client ID (0 for all):");

            if (idStr != null) {
                int cid = Integer.parseInt(idStr);

                List<Order> orders = (cid == 0)
                        ? clientService.getAllOrders()
                        : clientService.getOrdersByClientId(cid);

                StringBuilder sb = new StringBuilder("Orders List:\n");

                for (Order o : orders) {
                    sb.append(o.toString()).append("\n");
                }

                JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()));
            }
        });

        refreshBtn.addActionListener(e -> refreshClientTable());

        refreshClientTable();
    }

    private void refreshClientTable() {
        tableModel.setRowCount(0);

        for (Client c : clientService.getAllClients()) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress()
            });
        }
    }
}