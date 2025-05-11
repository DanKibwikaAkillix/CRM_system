import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.*;

public class ViewerDashboard extends JFrame {

    private String viewerEmail;
    private JPanel itemsPanel;

    public ViewerDashboard(String email) {
        this.viewerEmail = email;

        setTitle("Viewer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Available Products", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(headerLabel, BorderLayout.NORTH);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new GridLayout(0, 2, 10, 10));
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton viewProfileButton = new JButton("View Profile");
        viewProfileButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewProfileButton.setBackground(new Color(40, 167, 69));
        viewProfileButton.setForeground(Color.WHITE);
        viewProfileButton.setFocusPainted(false);
        viewProfileButton.addActionListener(e -> viewProfile());
        add(viewProfileButton, BorderLayout.SOUTH);

        loadItemsFromDatabase();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadItemsFromDatabase() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "SELECT * FROM Items";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("Id");
                String name = rs.getString("Name");
                double price = rs.getDouble("Price");
                int quantity = rs.getInt("Quantity");
                String imagePath = rs.getString("Image");

                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                itemPanel.setBackground(Color.WHITE);

                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                imageLabel.setPreferredSize(new Dimension(200, 150));

                if (imagePath != null && !imagePath.isEmpty()) {
                    File imgFile = new File(imagePath);
                    if (imgFile.exists()) {
                        ImageIcon icon = new ImageIcon(imagePath);
                        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        imageLabel.setText("Image not found");
                        System.err.println("Image file not found: " + imagePath);
                    }
                } else {
                    imageLabel.setText("No Image Available");
                    System.err.println("Empty image path for item ID: " + id);
                }

                JLabel nameLabel = new JLabel("Name: " + name);
                JLabel priceLabel = new JLabel("Price: $" + price);
                JLabel quantityLabel = new JLabel("Available: " + quantity);
                JButton buyButton = new JButton("Buy");
                buyButton.addActionListener((ActionEvent e) -> {
                    JOptionPane.showMessageDialog(this, "You selected to buy: " + name);
                });

                JPanel detailsPanel = new JPanel();
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.add(nameLabel);
                detailsPanel.add(priceLabel);
                detailsPanel.add(quantityLabel);
                detailsPanel.add(buyButton);

                itemPanel.add(imageLabel, BorderLayout.NORTH);
                itemPanel.add(detailsPanel, BorderLayout.CENTER);

                itemsPanel.add(itemPanel);
            }

            revalidate();
            repaint();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading items: " + ex.getMessage());
        }
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
