package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;

public class SideMenuPanel extends JPanel {
	private CardLayout cardLayout;
	private JPanel contentPanel;
	private JButton activeButton = null;
    private final Map<String, JButton> buttonMap = new HashMap<>();

	public SideMenuPanel(String activeViewName) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(new Color(33, 37, 41));
		setPreferredSize(new Dimension(200, 600));
		setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

		JLabel lblTitle = new JLabel("<html><div style='text-align:center;'>"
				+ "<span style='font-size:20px; font-weight:bold; " + "color:#FF6F00; text-shadow: 2px 2px #4E342E;'>"
				+ "Project Task Management<br>System</span></div></html>");

		lblTitle.setForeground(new Color(255, 255, 255));
		lblTitle.setAlignmentY(Component.LEFT_ALIGNMENT);
		lblTitle.setFont(new Font("Serif", Font.BOLD, 22));
		lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
		add(lblTitle);

		// Add menu buttons with spacing
		addButton("Project", "ProjectView");
		addSpacer();
		addButton("MileStone", "MilestoneView");
		addSpacer();
		addButton("Task", "TaskView");
		addSpacer();
		addButton("Attachment", "AttachmentView");
		addSpacer();
		addButton("Employee", "EmployeeView");
		addSpacer();
		addButton("Team", "TeamView");
		addSpacer();
		addButton("TeamMember", "TeamMember");
		addSpacer();
		addButton("Client", "ClientView");
		addSpacer();
		addButton("Notification", "NotificationView");
		addSpacer();
		addButton("Logout", "LoginView");

		add(Box.createVerticalGlue());

		// Activate the correct button initially
		setInitialActiveButton(activeViewName);

		contentPanel = new JPanel();
		cardLayout = new CardLayout();
		contentPanel.setLayout(cardLayout);
	}

	private void addButton(String title, String viewName) {
		JButton button = createMenuButton(title, viewName);
		buttonMap.put(viewName, button);
		add(button);
	}

	private void addSpacer() {
		add(Box.createRigidArea(new Dimension(0, 10)));
	}

	private void setInitialActiveButton(String viewName) {
		JButton button = buttonMap.get(viewName);
		if (button != null) {
			if (activeButton != null) {
				activeButton.setBackground(new Color(52, 58, 64));
			}
			activeButton = button;
			activeButton.setBackground(new Color(255, 111, 0));
		}
	}

	private JButton createMenuButton(String title, String viewName) {
		JButton button = new JButton(title);
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		button.setFont(new Font("SansSerif", Font.PLAIN, 14));
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(52, 58, 64));
		button.setOpaque(true);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setHorizontalAlignment(SwingConstants.LEFT);
		button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));

		// Hover effect
		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
			    if (button != activeButton) {
	                button.setBackground(new Color(255, 111, 0));
	            }
			}

			public void mouseExited(MouseEvent e) {
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

			// Open corresponding view in full screen
			JFrame frameToOpen = null;
			switch (viewName) {
			case "LoginView":
				frameToOpen = new LoginView();
				break;
			case "ProjectView":
				frameToOpen = new ProjectView();
				break;
			case "MilestoneView":
				 frameToOpen = new MilestoneView();
				break;
			case "TaskView":
				 frameToOpen = new TaskView();
				break;
			case "AttachmentView":
				frameToOpen = new AttachmentView();
				break;
			case "ClientView":
				frameToOpen = new ClientView();
				break;
			case "EmployeeView":
				frameToOpen = new EmployeeView();
				break;
			case "TeamView":
				frameToOpen = new TeamView();
				break;
			case "TeamMember":
				frameToOpen = new TeamMember();
				break;
			case "NotificationView":
				 frameToOpen = new NotificationView();
				break;
			default:
				JOptionPane.showMessageDialog(null, "Unknown view: " + viewName);
			}

			if (frameToOpen != null) {
				frameToOpen.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frameToOpen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frameToOpen.setVisible(true);
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
