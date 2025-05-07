import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ItemsCRUDFrame extends JFrame {

    private JTable itemsTable;
    private DefaultTableModel tableModel;

    public ItemsCRUDFrame() {
        setTitle("Item Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Manage Items", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Table model with column names
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Qty", "Actions"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only allow editing the "Actions" column
            }
        };

        itemsTable = new JTable(tableModel);
        itemsTable.setRowHeight(60);

        // Add custom button renderer and editor
        itemsTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        itemsTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(itemsTable), BorderLayout.CENTER);

        // Bottom button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Add New Item button
        JButton addNewItemButton = new JButton("Add New Item");
        addNewItemButton.setFont(new Font("Arial", Font.PLAIN, 16));
        addNewItemButton.setBackground(new Color(40, 167, 69));
        addNewItemButton.setForeground(Color.WHITE);
        addNewItemButton.setFocusPainted(false);
        addNewItemButton.addActionListener(e -> showItemForm());
        buttonPanel.add(addNewItemButton);

        // Return to Dashboard button
        JButton returnButton = new JButton("Return to Dashboard");
        returnButton.setFont(new Font("Arial", Font.PLAIN, 16));
        returnButton.setBackground(new Color(0, 123, 255));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(getTitle());  // Ensure AdminDashboard class exists
        });
        buttonPanel.add(returnButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);

        loadItems();
    }

    private void loadItems() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT * FROM Items";
            ResultSet rs = conn.prepareStatement(sql).executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                int quantity = rs.getInt("Quantity");
                tableModel.addRow(new Object[]{id, name, price, quantity, "Edit/Delete"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage());
        }
    }

    private void showItemForm() {
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Item Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(qtyField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String sql = "INSERT INTO Items (Name, Price, Quantity) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setDouble(2, Double.parseDouble(priceField.getText()));
                stmt.setInt(3, Integer.parseInt(qtyField.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item added.");
                loadItems();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    // ButtonRenderer for rendering the "Edit/Delete" buttons
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setText("Edit/Delete");
            setFocusPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // ButtonEditor to handle Edit/Delete actions
    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int rowIndex;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Edit/Delete");
            button.setFocusPainted(false);
            button.addActionListener(e -> onButtonClick());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            rowIndex = row;
            return button;
        }

        private void onButtonClick() {
            String[] options = {"Update", "Delete", "Cancel"};
            int choice = JOptionPane.showOptionDialog(ItemsCRUDFrame.this,
                    "Do you want to update or delete this item?",
                    "Select Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);

            if (choice == 0) {
                editItem(rowIndex);
            } else if (choice == 1) {
                deleteItem(rowIndex);
            }
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    private void editItem(int row) {
        String originalName = (String) tableModel.getValueAt(row, 1);
        double originalPrice = (double) tableModel.getValueAt(row, 2);
        int originalQty = (int) tableModel.getValueAt(row, 3);

        JTextField nameField = new JTextField(originalName);
        JTextField priceField = new JTextField(String.valueOf(originalPrice));
        JTextField qtyField = new JTextField(String.valueOf(originalQty));

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Item Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Quantity:"));
        panel.add(qtyField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String sql = "UPDATE Items SET Name=?, Price=?, Quantity=? WHERE ID=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setDouble(2, Double.parseDouble(priceField.getText()));
                stmt.setInt(3, Integer.parseInt(qtyField.getText()));
                stmt.setInt(4, (int) tableModel.getValueAt(row, 0));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item updated successfully.");
                loadItems();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error updating item: " + ex.getMessage());
            }
        }
    }

    private void deleteItem(int row) {
        int itemId = (int) tableModel.getValueAt(row, 0);

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this item?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String sql = "DELETE FROM Items WHERE ID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, itemId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item deleted successfully.");
                loadItems();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting item: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ItemsCRUDFrame::new);
    }
}
