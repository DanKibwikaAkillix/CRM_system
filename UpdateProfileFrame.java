import javax.swing.*;
import java.awt.*;

public class UpdateProfileFrame extends JFrame {
    public UpdateProfileFrame(String userEmail) {
        setTitle("Update Profile");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel nameLabel = new JLabel("Full Name:");
        JTextField nameField = new JTextField();

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("New Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(updateBtn);
        panel.add(cancelBtn);

        add(panel);
        setVisible(true);
    }

}
