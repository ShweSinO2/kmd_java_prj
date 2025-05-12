package view;

import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import config.MySqlQuery;
import controller.ProjectController;
import model.ProjectModel;

public class NotificationView extends JFrame {
	private JPanel rightPanel;
	private JPanel cardContainer;
	private GridBagConstraints gbc;
	Map<String, Integer> statusMap = new HashMap<>();
	Map<String, Integer> teamMap = new HashMap<>();
	Map<String, Integer> clientMap = new HashMap<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			NotificationView notiFrame = new NotificationView();
			notiFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			notiFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			notiFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public NotificationView() {
		setTitle("Notification List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel("NotificationView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		rightPanel = new JPanel();
//		rightPanel.setLayout(new GridBagLayout());
//	    rightPanel.setLayout(new GridLayout(0, 3, 10, 10));
//		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // ✅ respects card size
		rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		cardContainer = new JPanel();
		cardContainer.setLayout(new GridBagLayout());
		// cardContainer.setLayout(new FlowLayout(FlowLayout.LEFT));
//	    cardContainer.setBackground(Color.WHITE);

		gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // spacing between cards
		gbc.fill = GridBagConstraints.NONE;

		// Simulate a list of more than 9 cards (e.g., 12)
//	    for (int i = 1; i <= 1; i++) {
//	        JPanel card = new JPanel();
//	        card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//	        card.setPreferredSize(new Dimension(350, 250));
//	        card.setBackground(new Color(255, 255,255));
//	        card.add(new JLabel("Project " + i));
////	        rightPanel.add(card);
//	        
//	        gbc.gridx = i % 3;    
//	        gbc.gridy = i / 3;    
//	        cardContainer.add(card, gbc);
//	    }

		// Wrap in scroll pane
		JScrollPane scrollPane = new JScrollPane(cardContainer);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);

		rightPanel.setLayout(new BorderLayout());
		rightPanel.add(scrollPane, BorderLayout.WEST);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		showList();
	}

	public void showList() {
		ProjectController pc = new ProjectController();
		
		MySqlQuery.getComboData("status", "status_id", "status_name", statusMap);
		MySqlQuery.getComboData("team", "team_id", "team_name", teamMap);
		MySqlQuery.getComboData("client", "client_id", "name", clientMap);
		
		try {
			List<ProjectModel> list = pc.selectDeadlineProject();
			for (int i = 0; i < list.size(); i++) {
				ProjectModel pm = list.get(i);

				JPanel card = new JPanel();
				card.setPreferredSize(new Dimension(350, 200));
				card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 1), // existing
						BorderFactory.createEmptyBorder(10, 10, 10, 10) // padding: top, left, bottom, right
				));
				card.setBackground(Color.WHITE);
				card.setLayout(new BorderLayout(10, 10));

				// Title: Project Name
				JLabel titleLabel = new JLabel(pm.getProject_name(), SwingConstants.CENTER);
				titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
				titleLabel.setForeground(new Color(204, 89, 0));
				card.add(titleLabel, BorderLayout.NORTH);

				// Content Panel
				JPanel content = new JPanel();
				content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
				content.setBackground(Color.WHITE);
				content.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

				content.add(new JLabel("Description: " + pm.getDescription()));
				content.add(Box.createVerticalStrut(8));
				content.add(new JLabel("Start Date: " + pm.getStart_date()));
				content.add(Box.createVerticalStrut(8));
				content.add(new JLabel("End Date: " + pm.getEnd_date()));
				content.add(Box.createVerticalStrut(8));
				content.add(new JLabel("Status ID: " + getNameById(statusMap, pm.getStatus_id())));
				content.add(Box.createVerticalStrut(8));
				content.add(new JLabel("Team ID: " + getNameById(teamMap, pm.getTeam_id())));
				content.add(Box.createVerticalStrut(8));
				content.add(new JLabel("Client ID: " + getNameById(clientMap, pm.getClient_id())));
				content.add(Box.createVerticalStrut(8));

				card.add(content, BorderLayout.CENTER);

				gbc.gridx = i % 3;
				gbc.gridy = i / 3;
				cardContainer.add(card, gbc);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getNameById(Map<String, Integer> map, int id) {
	    for (Map.Entry<String, Integer> entry : map.entrySet()) {
	        if (entry.getValue() == id) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}

}
