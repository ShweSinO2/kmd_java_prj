package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class SideMenuPanel extends JPanel {
	private CardLayout cardLayout;
	private JPanel contentPanel;
	private JButton activeButton = null;

	public SideMenuPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(33, 37, 41));
		setPreferredSize(new Dimension(200, 600));
		setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

		JLabel lblTitle = new JLabel("<html><div style='text-align:center;'>"
				+ "<span style='font-size:20px; font-weight:bold; " + "color:#FF6F00; text-shadow: 2px 2px #4E342E;'>"
				+ "Project Management<br>System</span></div></html>");

		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setAlignmentY(Component.LEFT_ALIGNMENT);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 22));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		add(lblTitle);

		// Add menu buttons with spacing
		add(createMenuButton("Project", "ProjectView"));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createMenuButton("Task", "TaskView"));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createMenuButton("Attachment", "AttachmentView"));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createMenuButton("Employee", "EmployeeView"));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createMenuButton("Team", "TeamView"));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createMenuButton("Notification", "NotificationView"));
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(createMenuButton("Logout", "LoginView"));

//		add(createMenuButton("Employee"));
//		add(Box.createRigidArea(new Dimension(0, 10)));
//		add(createMenuButton("Project"));
//		add(Box.createRigidArea(new Dimension(0, 10)));
//		add(createMenuButton("Task"));
//		add(Box.createRigidArea(new Dimension(0, 10)));
//		add(createMenuButton("Team"));
//		add(Box.createRigidArea(new Dimension(0, 10)));
//		add(createMenuButton("Notification"));

		add(Box.createVerticalGlue());

		contentPanel = new JPanel();
		cardLayout = new CardLayout();
		contentPanel.setLayout(cardLayout);

		// Add views
//		contentPanel.add(new EmployeeView(), "employee");
//		contentPanel.add(new TaskView(), "task");
//		add(contentPanel, BorderLayout.CENTER);

	}

	private JButton createMenuButton(String text, String viewName) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		button.setFont(new Font("SansSerif", Font.PLAIN, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(52, 58, 64));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

		// Hover effect
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
//				button.setBackground(new Color(255, 111, 0));
			    if (button != activeButton) {
	                button.setBackground(new Color(255, 111, 0));
	            }
			}

			public void mouseExited(MouseEvent e) {
//				button.setBackground(new Color(52, 58, 64));
				   if (button != activeButton) {
		                button.setBackground(new Color(52, 58, 64));
		            }
			}
		});

		// Switch view on click
		button.addActionListener(e -> {
		    // Close current window
		    Window currentWindow = SwingUtilities.getWindowAncestor(button);
		    if (currentWindow != null) {
		        currentWindow.dispose();
		    }
		    
	        switch (viewName) {
	            case "LoginView":
	                new LoginView().setVisible(true);
	                break;
	            case "ProjectView":
	                new ProjectView().setVisible(true);
	                break;
	            case "TaskView":
//	                new TaskView().setVisible(true);
	                break;
	            case "AttachmentView":
	                new AttachmentView().setVisible(true);
	                break;
	            case "EmployeeView":
//	                new EmployeeView().setVisible(true);
	                break;
	            case "TeamView":
//	                new TeamView().setVisible(true);
	                break;
	            case "NotificationView":
//	                new NotificationView().setVisible(true);
	                break;
	            default:
	                JOptionPane.showMessageDialog(null, "Unknown view: " + viewName);
	        }

	        // 3. Update active button style
	        if (activeButton != null) {
	            activeButton.setBackground(new Color(52, 58, 64)); // reset previous
	        }
	        activeButton = button;
	        button.setBackground(new Color(255, 111, 0)); // active color
	    });

	    return button;
	}
}
