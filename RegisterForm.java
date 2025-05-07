import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;

public class RegisterForm extends JFrame {
    JTextField usernameField, emailField, profilePicturePathField;
    JPasswordField passwordField;
    JComboBox<String> privilegeBox;
    JButton selectImageButton;

    public RegisterForm() {
        setTitle("User Registration");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 123, 255), 0, getHeight(), new Color(255, 193, 7));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        panel.add(createLabel("Username:"));
        usernameField = new JTextField();
        styleTextField(usernameField);
        panel.add(usernameField);

        panel.add(createLabel("Email:"));
        emailField = new JTextField();
        styleTextField(emailField);
        panel.add(emailField);

        panel.add(createLabel("Password:"));
        passwordField = new JPasswordField();
        styleTextField(passwordField);
        panel.add(passwordField);

        panel.add(createLabel("Privilege:"));
        privilegeBox = new JComboBox<>(new String[]{"user", "admin", "salesPerson", "viewer"});
        panel.add(privilegeBox);

        panel.add(createLabel("Profile Picture:"));
        profilePicturePathField = new JTextField();
        profilePicturePathField.setEditable(false);
        styleTextField(profilePicturePathField);
        panel.add(profilePicturePathField);

        selectImageButton = new JButton("Select Image");
        styleButton(selectImageButton, new Color(0, 123, 255));
        selectImageButton.addActionListener(e -> selectImage());
        panel.add(selectImageButton);

        JButton registerBtn = new JButton("Register");
        styleButton(registerBtn, new Color(40, 167, 69));
        registerBtn.addActionListener(e -> registerUser());
        panel.add(registerBtn);

        JButton loginBtn = new JButton("Back to Login");
        styleButton(loginBtn, new Color(108, 117, 125));
        loginBtn.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        panel.add(loginBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 123, 255)));
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setPreferredSize(new Dimension(200, 30));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                File imagesFolder = new File("images");
                if (!imagesFolder.exists()) {
                    imagesFolder.mkdir();
                }

                String uniqueFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destinationFile = new File(imagesFolder, uniqueFileName);

                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                profilePicturePathField.setText(destinationFile.getPath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save image: " + ex.getMessage());
            }
        }
    }

    private void registerUser() {
        try (Connection conn = DBConnector.connect()) {
            String sql = "INSERT INTO Users (userName, Email, Password, status, Privilege, Profile_picture) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, usernameField.getText());
            stmt.setString(2, emailField.getText());
            stmt.setString(3, new String(passwordField.getPassword()));
            stmt.setString(4, "active");
            stmt.setString(5, privilegeBox.getSelectedItem().toString());

            String profilePicturePath = profilePicturePathField.getText();
            if (!profilePicturePath.isEmpty()) {
                stmt.setString(6, profilePicturePath);
            } else {
                stmt.setString(6, null);
            }

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "User registered successfully!");
                dispose();
                new LoginForm();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new RegisterForm();
    }
}
