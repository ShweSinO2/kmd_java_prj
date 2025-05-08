package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import config.MySqlQuery;
import controller.AttachmentController;
import controller.TeamMemberController;
import model.AttachmentModel;
import model.TeamMemberModel;

public class TeamMember extends JFrame {
	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblTeamMember;
	Map<String, Integer> teamMap = new HashMap<>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			TeamMember tmFrame = new TeamMember();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			tmFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			tmFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			tmFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TeamMember() {
		setTitle("Team Member");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel("TeamMember");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(null);

		JPanel formPanel = new JPanel();
		formPanel.setBackground(new Color(255, 255, 255));
		formPanel.setBounds(10, 0, 661, 145);
		formPanel.setLayout(null);
		
		rightPanel.add(formPanel);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(29, 85, 89, 23);
		formPanel.add(btnSave);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(155, 85, 89, 23);
		formPanel.add(btnUpdate);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(281, 85, 89, 23);
		formPanel.add(btnClear);
		
		JLabel lblTeamName = new JLabel(" Team Name");
		lblTeamName.setBounds(29, 32, 82, 14);
		formPanel.add(lblTeamName);
		
		JComboBox cboTeam = new JComboBox();
		cboTeam.setBounds(121, 24, 120, 31);
		MySqlQuery.addCoboBox("team", "team_id", "team_name", cboTeam, teamMap);
		formPanel.add(cboTeam);
		
		JComboBox<String> comboBox = new JComboBox<>(new String[]{"-Select-", "Option 1", "Option 2 (disabled)", "Option 3"});
		comboBox.setBounds(411, 24, 120, 31);;
		formPanel.add(comboBox);
		comboBox.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if ("Option 2 (disabled)".equals(value)) {
		            c.setEnabled(false);
		            c.setForeground(Color.GRAY);
		        } else {
		            c.setEnabled(true);
		            c.setForeground(Color.BLACK);
		        }
		        return c;
		    }
		});

		comboBox.addActionListener(e -> {
		    if ("Option 2 (disabled)".equals(comboBox.getSelectedItem())) {
		        comboBox.setSelectedIndex(0); // Revert to default
		        JOptionPane.showMessageDialog(null, "This member already add in a Team.");
		    }
		});
		
		JLabel lblNewLabel = new JLabel("Employee Name");
		lblNewLabel.setBounds(281, 32, 120, 14);
		formPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Position");
		lblNewLabel_1.setBounds(570, 32, 46, 14);
		formPanel.add(lblNewLabel_1);
		
		JComboBox cboPosition = new JComboBox();
		cboPosition.setModel(new DefaultComboBoxModel(new String[] {"-Select-", "Project Leader", "Member"}));
		cboPosition.setBounds(626, 24, 120, 31);
		formPanel.add(cboPosition);
		
		tblTeamMember = new JTable();
		tblTeamMember.setRowHeight(20);
		tblTeamMember.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				AttachmentModel am = new AttachmentModel();
//				
//				int row = tblTeamMember.rowAtPoint(e.getPoint());
//				int column = tblTeamMember.columnAtPoint(e.getPoint());
//
////				Attachment_id = (String) tblTeamMember.getValueAt(row, 0);
////				am.setAttachment_id(Integer.parseInt(Attachment_id));
//				am.setFilename((String) tblTeamMember.getValueAt(row, 1));
//				
			}
		});
		
		
		JScrollPane tableScrollPane = new JScrollPane(tblTeamMember);
		tableScrollPane.setBounds(10, 156, 664, 433);
		tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
		rightPanel.setLayout(null);
		
		rightPanel.add(tableScrollPane);
		
		rightPanel.addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e) {
		    	int padding = 16;
		    	int formHeight = 150;
		    	int formMarginBottom = 16;
		    	
		        int width = rightPanel.getWidth();
		        int height = rightPanel.getHeight();

		        int innerWidth = width - (padding * 2);
		        int innerHeight = height - (padding * 2);

		        int tableY = padding + formHeight + formMarginBottom;
		        int tableHeight = innerHeight - formHeight - formMarginBottom;

		        formPanel.setBounds(padding, padding, innerWidth, formHeight);
		        tableScrollPane.setBounds(padding, tableY, innerWidth, 500);
		    }
		});
		
		createTable();
		showList();
		getContentPane().add(rightPanel, BorderLayout.CENTER);
	}
	
	public void setColumnWidth(int index, int width) {
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) tblTeamMember.getColumnModel();
		TableColumn tc = tcm.getColumn(index);
		tc.setPreferredWidth(width);
	}
	
	public void createTable() {
		dtm.addColumn("Employee Name");
		dtm.addColumn("Team Name");
		dtm.addColumn("Position");
		dtm.addColumn("");
		tblTeamMember.setModel(dtm);
		tblTeamMember.setRowHeight(25);
		setColumnWidth(0, 20);
		setColumnWidth(1, 200);
		setColumnWidth(2, 150);
		setColumnWidth(3, 150);

	    // Customize table header
	    JTableHeader header = tblTeamMember.getTableHeader();
	    header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Set header height
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment
	    
	 // Apply center alignment to specific columns
	    tblTeamMember.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	    tblTeamMember.getColumnModel().getColumn(2).setCellRenderer(createButtonCellRenderer("Delete"));
	    
	    // Set custom header renderer
	    DefaultTableCellRenderer headerRenderer = createHeaderRenderer();
	    
	    // Apply header renderer to all columns
	    for (int i = 0; i < tblTeamMember.getColumnModel().getColumnCount(); i++) {
	        tblTeamMember.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
	    }
	}
	
	private DefaultTableCellRenderer createButtonCellRenderer(final String buttonText) {
	    return new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                boolean isSelected, boolean hasFocus, int row, int column) {
	        	
	            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            setHorizontalAlignment(SwingConstants.CENTER);

	            JLabel label = new JLabel(buttonText);
	            label.setHorizontalAlignment(SwingConstants.CENTER); // Center text
	            
	            // Style the label like a button
	            label.setPreferredSize(new Dimension(30, 15));
//	            label.setBackground(new Color(230, 100, 0));
	            label.setBackground(new Color(255, 255, 255));
          	  	label.setForeground(new Color(230, 100, 0));
          	  	label.setCursor(new Cursor(Cursor.HAND_CURSOR));
	            
	            //download design
	            if(buttonText == "Delete") {
		            label.setBackground(new Color(220, 53, 69));
		            label.setForeground(Color.WHITE);
	                label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
//	                label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
	            }
	            
	            label.setFont(new Font("Arial", Font.BOLD, 12));
	            label.setOpaque(true);

	            return label;
	        }
	    };
	}
	
	private DefaultTableCellRenderer createHeaderRenderer() {
	    return new DefaultTableCellRenderer() {
	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                boolean isSelected, boolean hasFocus, int row, int column) {
	            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	            c.setBackground(new Color(230, 100, 0));
	            c.setForeground(Color.WHITE);
	            setHorizontalAlignment(SwingConstants.CENTER);
	            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
	            setFont(new Font("Arial", Font.BOLD, 12));
	            return c;
	        }
	    };
	}
	
	public void showList() {
		String data[] = new String[9];
		String relatedName = null;
        
		TeamMemberController tmc = new TeamMemberController();
		try {
			List<TeamMemberModel> list = tmc.selectall();
			dtm.setRowCount(0);
			for (TeamMemberModel tmm : list) {		        
				data[0] = Integer.toString(tmm.getTeam_id());
				data[1] = tmm.getEmployee_id();
				data[3] = "Position";
				data[4] = "Delete";
//				data[5] = "Delete";
				
//				if ("Project".equals(am.getRelated_type())) {
//					data[2] = getNameById(projectMap, am.getRelated_id());
//			    } else if ("Task".equals(am.getRelated_type())) {
//			        data[2] = getNameById(taskMap, am.getRelated_id());
//			    }
//				
//				data[3] = am.getRelated_type();
//				data[4] = "Download";
//				data[5] = "Delete";
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//get id from database and show name by Id
	public static String getNameById(Map<String, Integer> map, int id) {
	    for (Map.Entry<String, Integer> entry : map.entrySet()) {
	        if (entry.getValue() == id) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
}
