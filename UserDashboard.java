import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboard extends JFrame {
    JButton viewProfileButton, viewOrdersButton, logoutButton;

    public UserDashboard() {
        setTitle("User Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel with a Welcome Message
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 123, 255));
        JLabel welcomeLabel = new JLabel("Welcome to your Dashboard!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Main Panel with Buttons for Different Actions
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // View Profile Button
        viewProfileButton = new JButton("View Profile");
        styleButton(viewProfileButton);
        viewProfileButton.addActionListener(e -> viewProfile());
        mainPanel.add(viewProfileButton);

        // View Orders Button
        viewOrdersButton = new JButton("View Orders");
        styleButton(viewOrdersButton);
        viewOrdersButton.addActionListener(e -> viewOrders());
        mainPanel.add(viewOrdersButton);

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

    // Method for viewing profile
    private void viewProfile() {
        JOptionPane.showMessageDialog(this, "Displaying User Profile...");
        // Add functionality to display the user's profile
    }

    // Method for viewing orders
    private void viewOrders() {
        JOptionPane.showMessageDialog(this, "Displaying Orders...");
        // Add functionality to display the user's orders
    }

    // Logout method
    private void logout() {
        JOptionPane.showMessageDialog(this, "Logging out...");
        // Add logic for logging out (close current session, etc.)
        dispose();
        new LoginForm(); // Redirect to the login page
    }

    public static void main(String[] args) {
        new UserDashboard();
    }
}
