import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDashboard extends JFrame {
    JButton viewProfileButton, viewOrdersButton, logoutButton;
    String userEmail;

    public UserDashboard(String email) {
        this.userEmail = email;
        setTitle("User Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 123, 255));
        JLabel welcomeLabel = new JLabel("Welcome, " + userEmail + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel);
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        viewProfileButton = new JButton("View Profile");
        styleButton(viewProfileButton);
        viewProfileButton.addActionListener(e -> viewProfile());
        mainPanel.add(viewProfileButton);

        viewOrdersButton = new JButton("View Orders");
        styleButton(viewOrdersButton);
        viewOrdersButton.addActionListener(e -> viewOrders());
        mainPanel.add(viewOrdersButton);

        logoutButton = new JButton("Logout");
        styleButton(logoutButton);
        logoutButton.addActionListener(e -> logout());
        mainPanel.add(logoutButton);

        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(40, 167, 69));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
    }

    private void viewProfile() {
        JOptionPane.showMessageDialog(this, "Displaying User Profile for: " + userEmail);
    }

    private void viewOrders() {
        JOptionPane.showMessageDialog(this, "Displaying Orders for: " + userEmail);
    }

    private void logout() {
        JOptionPane.showMessageDialog(this, "Logging out...");
        dispose();
        new LoginForm();
    }

    public static void main(String[] args) {
    }
}
