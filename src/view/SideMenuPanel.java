package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SideMenuPanel extends JPanel {
    public SideMenuPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(200, 600));
        
        JLabel lblNewLabel = new JLabel("<html> Project Management <br> System<br><br></html>");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(lblNewLabel);

        // Add menu buttons
        add(createMenuButton("Employee"));
        add(createMenuButton("Project"));
        add(createMenuButton("Task"));
        add(createMenuButton("Team"));
        add(createMenuButton("Notification"));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        button.setMaximumSize(new Dimension(180, 40));
        return button;
    }
}
