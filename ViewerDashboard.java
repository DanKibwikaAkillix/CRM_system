import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewerDashboard extends JFrame {

    private String viewerEmail;

    public ViewerDashboard(String email) {
        this.viewerEmail = email;

        setTitle("Viewer Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Welcome to the Viewer Dashboard", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);

        JButton viewProfileButton = new JButton("View Profile");
        viewProfileButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewProfileButton.setBackground(new Color(40, 167, 69));
        viewProfileButton.setForeground(Color.WHITE);
        viewProfileButton.setFocusPainted(false);
        viewProfileButton.addActionListener(e -> viewProfile());
        add(viewProfileButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void viewProfile() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT * FROM Users WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, viewerEmail);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String company = rs.getString("Company");

                // Display user details in a dialog box
                String userProfile = "Name: " + name + "\n" +
                                     "Surname: " + surname + "\n" +
                                     "Company: " + company;

                JOptionPane.showMessageDialog(this, userProfile, "User Profile", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No profile found for the given email.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

   
}
