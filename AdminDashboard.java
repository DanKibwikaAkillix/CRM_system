import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    JButton manageUsersButton, viewReportsButton, logoutButton;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel with a Welcome Message
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 123, 255));
        JLabel welcomeLabel = new JLabel("Welcome to the Admin Dashboard!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel with Buttons for Different Admin Actions
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Manage Users Button
        manageUsersButton = new JButton("Manage Users");
        styleButton(manageUsersButton);
        manageUsersButton.addActionListener(e -> manageUsers());
        mainPanel.add(manageUsersButton);

        // View Reports Button
        viewReportsButton = new JButton("View Reports");
        styleButton(viewReportsButton);
        viewReportsButton.addActionListener(e -> viewReports());
        mainPanel.add(viewReportsButton);

        // Logout Button
        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> logout());
        mainPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    // Button styling
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(40, 167, 69));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    // Method to manage users (admin can add, delete, or update users)
    private void manageUsers() {
        JOptionPane.showMessageDialog(this, "Managing Users...");
        // Add functionality to manage users (e.g., adding/removing users, changing privileges)
    }

    // Method to view reports (admin can view system or usage reports)
    private void viewReports() {
        JOptionPane.showMessageDialog(this, "Displaying Reports...");
        // Add functionality to view reports (e.g., sales reports, user activity reports)
    }

    // Logout method
    private void logout() {
        JOptionPane.showMessageDialog(this, "Logging out...");
        // Add logic for logging out (close current session, etc.)
        dispose();
        new LoginForm(); // Redirect to the login page
    }

    public static void main(String[] args) {
        new AdminDashboard();
    }
}
