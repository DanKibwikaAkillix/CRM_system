import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateProfileFrame extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private String userEmail;

    public UpdateProfileFrame(String userEmail) {
        this.userEmail = userEmail;

        setTitle("Update Profile");
        setSize(500, 280);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("New Password:");
        passwordField = new JPasswordField();

        JButton updateBtn = new JButton("Update");
        JButton backBtn = new JButton("Back to Dashboard");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(updateBtn);
        panel.add(backBtn);

        add(panel);
        setVisible(true);

        fetchUserData();

        updateBtn.addActionListener(e -> updateUserProfile());
        backBtn.addActionListener(e -> {
            new AdminDashboard(userEmail);
            dispose();
        });
    }

    private void fetchUserData() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT userName, Email, Password FROM Users WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usernameField.setText(rs.getString("userName"));
                emailField.setText(rs.getString("Email"));
                passwordField.setText(rs.getString("Password"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage());
        }
    }

    private void updateUserProfile() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnector.connect()) {
            String sql = "UPDATE Users SET userName = ?, Email = ?, Password = ? WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, userEmail);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
                userEmail = email;
            } else {
                JOptionPane.showMessageDialog(this, "No changes made.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Update failed: " + e.getMessage());
        }
    }
}
