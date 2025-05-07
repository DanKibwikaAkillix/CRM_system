import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class SalespersonCRUDFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public SalespersonCRUDFrame() {
        setTitle("Salesperson Management");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add New Salesperson");
        JButton dashboardButton = new JButton("Return to Dashboard");
        topPanel.add(addButton);
        topPanel.add(dashboardButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Privilege", "Actions"}, 0);
        table = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table.setRowHeight(40);
        table.getColumn("Actions").setCellRenderer((TableCellRenderer) new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(table), BorderLayout.CENTER);

        loadSalespersons();

        addButton.addActionListener(e -> openAddForm());

        dashboardButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(getTitle());
        });

        setVisible(true);
    }

    private void loadSalespersons() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT Id, userName, Email, Privilege FROM Users WHERE Privilege = 'salesPerson'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("Id"),
                        rs.getString("userName"),
                        rs.getString("Email"),
                        rs.getString("Privilege"),
                        "Actions"
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openAddForm() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passField = new JPasswordField();

        Object[] message = {
            "Name:", nameField,
            "Email:", emailField,
            "Password:", passField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Salesperson", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String sql = "INSERT INTO Users (userName, Email, Password, Privilege) VALUES (?, ?, ?, 'salesPerson')";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, emailField.getText());
                stmt.setString(3, new String(passField.getPassword()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Salesperson added!");
                loadSalespersons();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void openUpdateForm(int userId, String name, String email) {
        JTextField nameField = new JTextField(name);
        JTextField emailField = new JTextField(email);

        Object[] message = {
            "Name:", nameField,
            "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Update Salesperson", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String sql = "UPDATE Users SET userName = ?, Email = ? WHERE Id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, emailField.getText());
                stmt.setInt(3, userId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Salesperson updated!");
                loadSalespersons();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void confirmAndDelete(int userId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this salesperson?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnector.connect()) {
                String sql = "DELETE FROM Users WHERE Id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Salesperson deleted!");
                loadSalespersons();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(updateButton);
            add(deleteButton);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel = new JPanel();
        protected JButton updateButton = new JButton("Update");
        protected JButton deleteButton = new JButton("Delete");
        private int selectedRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(updateButton);
            panel.add(deleteButton);

            updateButton.addActionListener(e -> {
                stopCellEditing();
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 1);
                String email = (String) tableModel.getValueAt(selectedRow, 2);
                openUpdateForm(id, name, email);
            });

            deleteButton.addActionListener(e -> {
                stopCellEditing();
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                confirmAndDelete(id);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;
            return panel;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }

}
