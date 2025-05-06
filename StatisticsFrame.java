import javax.swing.*;
import java.awt.*;

public class StatisticsFrame extends JFrame {
    public StatisticsFrame() {
        setTitle("System Statistics");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("System Records Statistics", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel customersLabel = new JLabel("Total Customers: 120");
        JLabel salesLabel = new JLabel("Total Salespeople: 45");
        JLabel itemsLabel = new JLabel("Total Items: 234");
        JLabel usersLabel = new JLabel("Total Users: 10");

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(customersLabel);
        panel.add(salesLabel);
        panel.add(itemsLabel);
        panel.add(usersLabel);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new StatisticsFrame();
    }
}
