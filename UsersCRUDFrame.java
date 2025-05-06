import javax.swing.*;
import java.awt.*;

public class UsersCRUDFrame extends JFrame {
    public UsersCRUDFrame() {
        setTitle("Manage Users");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("User Management", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(header, BorderLayout.NORTH);

        JButton createBtn = new JButton("Add User");
        JButton readBtn = new JButton("View Users");
        JButton updateBtn = new JButton("Update User");
        JButton deleteBtn = new JButton("Delete User");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(createBtn);
        buttonPanel.add(readBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new UsersCRUDFrame();
    }
}
