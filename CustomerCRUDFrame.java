import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerCRUDFrame extends JFrame {
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton addCustomerButton;

    public CustomerCRUDFrame() {
        setTitle("Customer Management");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        addCustomerButton = new JButton("➕ Create New Customer");
        addCustomerButton.addActionListener(e -> openCustomerForm(null));
        topPanel.add(addCustomerButton);

        JButton backButton = new JButton("← Go Back to Dashboard");
        backButton.addActionListener(e -> {
            dispose();
            new AdminDashboard(getTitle());
        });
        topPanel.add(backButton);

        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"Name", "Surname", "Lastname", "Email", "Phone", "Address", "Company", "Status", "Category", "Actions"};
        tableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(tableModel) {
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };

        customerTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        customerTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        loadCustomers();
        setVisible(true);
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        try (Connection conn = DBConnector.connect()) {
            String query = "SELECT * FROM Customer";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("lastname"),
                    rs.getString("email"),
                    rs.getString("phonenumber"),
                    rs.getString("address1"),
                    rs.getString("company"),
                    rs.getString("status"),
                    rs.getString("category"),
                    "Update/Delete"
                };
                tableModel.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    private void openCustomerForm(String emailToUpdate) {
        JDialog dialog = new CustomerFormDialog(
            this,
            emailToUpdate != null ? "Update Customer" : "New Customer",
            emailToUpdate != null,
            emailToUpdate
        );
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                loadCustomers();
            }
        });
    }

    private void deleteCustomer(String email) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "DELETE FROM Customer WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Customer Deleted");
            loadCustomers();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Update/Delete");
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String email;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Update/Delete");
            button.setOpaque(true);
            button.addActionListener(e -> {
                int row = customerTable.getSelectedRow();
                if (row != -1) {
                    email = (String) customerTable.getValueAt(row, 3);
                    String[] options = {"Update", "Delete"};
                    int choice = JOptionPane.showOptionDialog(
                        null,
                        "Choose an action for: " + email,
                        "Action",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]
                    );

                    if (choice == 0) openCustomerForm(email);
                    else if (choice == 1) deleteCustomer(email);
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        public Object getCellEditorValue() {
            return "Update/Delete";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerCRUDFrame::new);
    }
}
