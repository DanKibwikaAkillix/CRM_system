import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class SalespersonCRUDFrame extends JFrame {

    private JTextField nameField, emailField, departmentField;
    private JButton createButton, updateButton, deleteButton;
    private JComboBox<String> salespersonList;

    public SalespersonCRUDFrame() {
        setTitle("CRUD Salesperson");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Salesperson Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Salesperson Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        formPanel.add(departmentField);

        // Buttons for CRUD operations
        createButton = new JButton("Create Salesperson");
        createButton.addActionListener(this::createSalesperson);
        formPanel.add(createButton);

        updateButton = new JButton("Update Salesperson");
        updateButton.addActionListener(this::updateSalesperson);
        formPanel.add(updateButton);

        deleteButton = new JButton("Delete Salesperson");
        deleteButton.addActionListener(this::deleteSalesperson);
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.CENTER);

        // Salesperson selection dropdown
        salespersonList = new JComboBox<>();
        loadSalespersons();
        add(salespersonList, BorderLayout.NORTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadSalespersons() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT email FROM Salesperson";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                salespersonList.addItem(rs.getString("email"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createSalesperson(ActionEvent e) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "INSERT INTO Salesperson (name, email, department) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, emailField.getText());
            stmt.setString(3, departmentField.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salesperson Created Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateSalesperson(ActionEvent e) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "UPDATE Salesperson SET name = ?, department = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, departmentField.getText());
            stmt.setString(3, (String) salespersonList.getSelectedItem());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salesperson Updated Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteSalesperson(ActionEvent e) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "DELETE FROM Salesperson WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, (String) salespersonList.getSelectedItem());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salesperson Deleted Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new SalespersonCRUDFrame();
    }
}
