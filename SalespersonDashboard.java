import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SalespersonDashboard extends JFrame {

    private String salespersonEmail;

    public SalespersonDashboard(String email) {
        this.salespersonEmail = email;

        setTitle("Salesperson Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Welcome to the Salesperson Dashboard", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        add(label, BorderLayout.CENTER);

        JButton viewSalesButton = new JButton("View Sales");
        viewSalesButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewSalesButton.setBackground(new Color(40, 167, 69));
        viewSalesButton.setForeground(Color.WHITE);
        viewSalesButton.setFocusPainted(false);
        viewSalesButton.addActionListener(e -> viewSales());
        add(viewSalesButton, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void viewSales() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT * FROM Sales WHERE SalespersonEmail = ?"; // Assuming the Sales table links to the salesperson
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, salespersonEmail);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Fetch and display sales data
                System.out.println("Sale ID: " + rs.getInt("Id"));
                System.out.println("Sale Amount: " + rs.getDouble("Amount"));
                // You can display more details or integrate this with the GUI
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

   
}
