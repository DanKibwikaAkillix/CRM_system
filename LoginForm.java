import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField emailField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;

    public LoginForm() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 123, 255), 0, getHeight(), new Color(255, 193, 7));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(Color.WHITE);
        panel.add(emailLabel);
        emailField = new JTextField();
        styleTextField(emailField);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(40, 167, 69));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> loginUser());
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBackground(new Color(0, 123, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterForm();
        });
        panel.add(registerButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setPreferredSize(new Dimension(200, 30));
    }

    private void styleTextField(JPasswordField passwordField) {
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255)));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordField.setPreferredSize(new Dimension(200, 30));
    }

    private void loginUser() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String privilege = rs.getString("Privilege");
                JOptionPane.showMessageDialog(this, "Welcome " + email + "!");

                switch (privilege.toLowerCase()) {
                    case "admin":
                        new AdminDashboard(email);
                        break;
                    case "user":
                        new UserDashboard(email);
                        break;
                    case "salesperson":
                        new SalespersonDashboard(email);
                        break;
                    case "viewer":
                        new ViewerDashboard(email);
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "Unknown user role: " + privilege);
                        return;
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}
