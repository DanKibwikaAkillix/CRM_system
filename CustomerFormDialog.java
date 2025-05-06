import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerFormDialog extends JDialog {
    private JTextField nameField, surnameField, lastnameField, emailField, phoneField, addressField, companyField;
    private JComboBox<String> statusComboBox, categoryComboBox;
    private JButton saveButton, cancelButton;
    private final boolean isUpdate;
    private final String originalEmail;

    public CustomerFormDialog(JFrame parent, String title, boolean isUpdate, String email) {
        super(parent, title, true);
        this.isUpdate = isUpdate;
        this.originalEmail = email;

        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        if (isUpdate && email != null) loadCustomer(email);

        saveButton.addActionListener(e -> saveCustomer());
        cancelButton.addActionListener(e -> dispose());

        setVisible(true);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nameField = addField(panel, "Name:");
        surnameField = addField(panel, "Surname:");
        lastnameField = addField(panel, "Lastname:");
        emailField = addField(panel, "Email:");
        phoneField = addField(panel, "Phone:");
        addressField = addField(panel, "Address:");
        companyField = addField(panel, "Company:");

        panel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"active", "inactive"});
        panel.add(statusComboBox);

        panel.add(new JLabel("Category:"));
        categoryComboBox = new JComboBox<>(new String[]{"vip", "regular", "new"});
        panel.add(categoryComboBox);

        return panel;
    }

    private JTextField addField(JPanel panel, String label) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField();
        panel.add(field);
        return field;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        panel.add(saveButton);
        panel.add(cancelButton);
        return panel;
    }

    private void loadCustomer(String email) {
        String query = "SELECT * FROM Customer WHERE email = ?";
        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                surnameField.setText(rs.getString("surname"));
                lastnameField.setText(rs.getString("lastname"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phonenumber"));
                addressField.setText(rs.getString("address1"));
                companyField.setText(rs.getString("company"));
                statusComboBox.setSelectedItem(rs.getString("status"));
                categoryComboBox.setSelectedItem(rs.getString("category"));
            }
        } catch (SQLException ex) {
            showError("Error loading customer: " + ex.getMessage());
        }
    }

    private void saveCustomer() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String lastname = lastnameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String company = companyField.getText();
        String status = (String) statusComboBox.getSelectedItem();
        String category = (String) categoryComboBox.getSelectedItem();

        String sql = isUpdate
            ? "UPDATE Customer SET name=?, surname=?, lastname=?, email=?, phonenumber=?, address1=?, company=?, status=?, category=? WHERE email=?"
            : "INSERT INTO Customer (name, surname, lastname, email, phonenumber, address1, company, status, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, surname);
            stmt.setString(3, lastname);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, address);
            stmt.setString(7, company);
            stmt.setString(8, status);
            stmt.setString(9, category);
            if (isUpdate) stmt.setString(10, originalEmail);

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, isUpdate ? "Customer updated successfully." : "Customer created successfully.");
            dispose();

        } catch (SQLException ex) {
            showError("Error saving customer: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
