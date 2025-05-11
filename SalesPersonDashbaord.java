import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalesPersonDashbaord extends JFrame {

    private String userEmail;

    public SalesPersonDashbaord(String userEmail) {
        this.userEmail = userEmail;
        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.CENTER);

        JPanel sidePanel = createSidePanel();
        add(sidePanel, BorderLayout.EAST);

        setVisible(true);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(60, 63, 65));
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));

        JLabel nameLabel = new JLabel("Customer Relationship  Management (CRM)");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 22));
        nameLabel.setForeground(Color.WHITE);

        JLabel profileLabel = new JLabel();
        profileLabel.setPreferredSize(new Dimension(80, 80));

        fetchUserProfile(nameLabel, profileLabel);

        JButton logoutButton = new JButton(new ImageIcon("images/logout.png"));
        logoutButton.setContentAreaFilled(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusable(false);
        logoutButton.addActionListener(e -> logout());

        ImageIcon logoutIcon = (ImageIcon) logoutButton.getIcon();
        Image logoutImage = logoutIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        logoutButton.setIcon(new ImageIcon(logoutImage));

        JPanel topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.add(logoutButton, BorderLayout.WEST);
        topRightPanel.add(profileLabel, BorderLayout.EAST);

        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 4 stats instead of 6
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel totalCustomers = createStatLabel("Total Customers: 0");
        JLabel activeCustomers = createStatLabel("Active Customers: 0");
        JLabel inactiveCustomers = createStatLabel("Inactive Customers: 0");
        JLabel totalItems = createStatLabel("Total Products: 0");

        statsPanel.add(totalCustomers);
        statsPanel.add(activeCustomers);
        statsPanel.add(inactiveCustomers);
        statsPanel.add(totalItems);

        loadStatistics(totalCustomers, activeCustomers, inactiveCustomers, totalItems);

        return statsPanel;
    }

    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setPreferredSize(new Dimension(150, 60));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    private JPanel createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        JButton manageCustomersButton = createSideButton("Manage Customers", e -> openCustomerCRUD());
        JButton manageItemsButton = createSideButton("Manage Products", e -> openItemsCRUD());
        JButton manageProfileButton = createSideButton("Manage My Profile", e -> openProfileManagement());

        sidePanel.add(manageCustomersButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(manageItemsButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(manageProfileButton);

        return sidePanel;
    }

    private JButton createSideButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        button.setPreferredSize(new Dimension(250, 50));
        button.setFocusable(false);
        button.setContentAreaFilled(true);
        button.addActionListener(listener);
        return button;
    }

    private void openCustomerCRUD() {
        new CustomerCRUDFrameSalePerson();
        dispose();
    }

    private void openItemsCRUD() {
        new ItemsCRUDFrameSalePerson();
        dispose();
    }

    private void openProfileManagement() {
        new UpdateProfileFrameSalePerson(userEmail);
        dispose();
    }

    private void fetchUserProfile(JLabel nameLabel, JLabel profileLabel) {
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT Name, Profilepicture FROM Users WHERE Email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                nameLabel.setText(" Customer Relationship  Management (CRM)" + name + "!");
                String picturePath = rs.getString("Profilepicture");

                if (picturePath != null && !picturePath.isEmpty()) {
                    ImageIcon imageIcon = new ImageIcon(picturePath);
                    Image img = imageIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    profileLabel.setIcon(new ImageIcon(img));
                } else {
                    profileLabel.setText("Manage My Profile");
                    profileLabel.setIcon(null);
                }

                profileLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                profileLabel.setHorizontalAlignment(SwingConstants.CENTER);
                profileLabel.setVerticalAlignment(SwingConstants.CENTER);
                profileLabel.setPreferredSize(new Dimension(80, 80));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStatistics(JLabel totalCustomers, JLabel activeCustomers, JLabel inactiveCustomers,
                                JLabel totalItems) {
        try (Connection conn = DBConnector.connect()) {
            Statement stmt = conn.createStatement();

            totalCustomers.setText("Total Customers: " + getCount(stmt, "Customer"));
            activeCustomers.setText("Active Customers: " + getCount(stmt, "Customer WHERE status = 'active'"));
            inactiveCustomers.setText("Inactive Customers: " + getCount(stmt, "Customer WHERE status = 'inactive'"));
            totalItems.setText("Total Products: " + getCount(stmt, "Items"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getCount(Statement stmt, String table) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    private void logout() {
        new LoginForm();
        dispose();
    }
}
