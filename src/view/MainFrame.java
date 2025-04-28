package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainFrame() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create CardLayout panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Add different views
        contentPanel.add(new ProjectView(), "Home");
        contentPanel.add(new LoginView(), "Employee");
        contentPanel.add(new ProjectView(), "Project");
        contentPanel.add(new LoginView(), "Team");

        // Add side menu with button actions
//        SideMenuPanel sideMenu = new SideMenuPanel(this);
//        add(sideMenu, BorderLayout.WEST);
//        add(contentPanel, BorderLayout.CENTER);
    }

    // Switch panel method
    public void showPanel(String name) {
        cardLayout.show(contentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
