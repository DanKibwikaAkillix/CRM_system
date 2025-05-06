import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class ItemsCRUDFrame extends JFrame {

    private JTextField itemNameField, itemPriceField;
    private JButton createButton, updateButton, deleteButton;
    private JComboBox<String> itemList;

    public ItemsCRUDFrame() {
        setTitle("CRUD Items");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Item Name:"));
        itemNameField = new JTextField();
        formPanel.add(itemNameField);

        formPanel.add(new JLabel("Item Price:"));
        itemPriceField = new JTextField();
        formPanel.add(itemPriceField);

        // Buttons for CRUD operations
        createButton = new JButton("Create Item");
        createButton.addActionListener(this::createItem);
        formPanel.add(createButton);

        updateButton = new JButton("Update Item");
        updateButton.addActionListener(this::updateItem);
        formPanel.add(updateButton);

        deleteButton = new JButton("Delete Item");
        deleteButton.addActionListener(this::deleteItem);
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.CENTER);

        // Item selection dropdown
        itemList = new JComboBox<>();
        loadItems();
        add(itemList, BorderLayout.NORTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadItems() {
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT name FROM Items";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                itemList.addItem(rs.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createItem(ActionEvent e) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "INSERT INTO Items (name, price) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, itemNameField.getText());
            stmt.setString(2, itemPriceField.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Created Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updateItem(ActionEvent e) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "UPDATE Items SET price = ? WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, itemPriceField.getText());
            stmt.setString(2, itemNameField.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Updated Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deleteItem(ActionEvent e) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "DELETE FROM Items WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, (String) itemList.getSelectedItem());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Item Deleted Successfully");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ItemsCRUDFrame();
    }
}
