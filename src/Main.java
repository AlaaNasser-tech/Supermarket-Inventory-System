import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import service.AdminService;
import service.ClientService;
import ui.ApplicationConsoleUI;
import ui.MainDashboard;

public class Main {

    public static void main(String[] args) {

        AdminService adminService = new AdminService();
        ClientService clientService = new ClientService(adminService);

        String[] options = {"GUI Mode", "Console Mode"};

        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Choose Application Mode:",
                "Start System",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == null) return;

        if (choice.equals("GUI Mode")) {

            SwingUtilities.invokeLater(() -> {
                MainDashboard dashboard = new MainDashboard();
                dashboard.setVisible(true);
            });

        } else {

            ApplicationConsoleUI app =
                    new ApplicationConsoleUI(adminService, clientService);
            app.start();
        }
    }
}