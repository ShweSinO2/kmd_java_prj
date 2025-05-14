package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import controller.ProjectController;
import controller.TaskController;
import controller.TeamMemberController;
import model.AttachmentModel;
import model.ProjectModel;
import model.TaskModel;
import model.TeamMemberModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TeamMember extends JFrame {
	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblTeamMember;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnClear;
	private String employeeName;
	private JComboBox<String> cboEmployee;
	private JComboBox cboTeam;
	private JComboBox cboPosition;
	private JComboBox cboTeamSearch;
	Map<String, Integer> teamMap = new HashMap<>();
	Map<String, String> employeeMap = new HashMap<>();

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
		
		JLabel lblTeamName = new JLabel(" Team Name");
		lblTeamName.setBounds(29, 32, 82, 14);
		formPanel.add(lblTeamName);
		
		cboTeam = new JComboBox();
		cboTeam.setBounds(121, 24, 120, 31);
		MySqlQuery.addCoboBox("team", "team_id", "team_name", cboTeam, teamMap);
		formPanel.add(cboTeam);
		
		cboEmployee = new JComboBox<>();
		cboEmployee.setBounds(411, 24, 120, 31);
		MySqlQuery.addCoboBoxEmployee("employee", "employee_id", "name", cboEmployee, employeeMap);
		formPanel.add(cboEmployee);
		
		cboEmployee.setRenderer(new DefaultListCellRenderer() {
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

		cboEmployee.addActionListener(e -> {
		    if ("Option 2 (disabled)".equals(cboEmployee.getSelectedItem())) {
		    	cboEmployee.setSelectedIndex(0); // Revert to default
		        JOptionPane.showMessageDialog(null, "This member already add in a Team.");
		    }
		});
		
		JLabel lblNewLabel = new JLabel("Employee Name");
		lblNewLabel.setBounds(281, 32, 120, 14);
		formPanel.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Position");
		lblNewLabel_1.setBounds(570, 32, 46, 14);
		formPanel.add(lblNewLabel_1);
		
		cboPosition = new JComboBox();
		cboPosition.setModel(new DefaultComboBoxModel(new String[] {"-Select-", "Project Leader", "Member"}));
		cboPosition.setBounds(626, 24, 120, 31);
		formPanel.add(cboPosition);
		
		tblTeamMember = new JTable();
		tblTeamMember.setRowHeight(20);
		tblTeamMember.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblTeamMember.rowAtPoint(e.getPoint());
		        int column = tblTeamMember.columnAtPoint(e.getPoint());
				TeamMemberModel tmm = new TeamMemberModel();
//				
				employeeName = (String)tblTeamMember.getValueAt(row, 0);
				cboEmployee.setSelectedItem(employeeName);
				
				String teamName = (String)tblTeamMember.getValueAt(row, 1);
				cboTeam.setSelectedItem(teamName);
				
				String position = (String)tblTeamMember.getValueAt(row, 2);
				cboPosition.setSelectedItem(position);	
				
				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				
				//delete row for team member
		        if (column == 3) {
		        	DefaultTableModel model = (DefaultTableModel) tblTeamMember.getModel();
	                String employeeId = (String) model.getValueAt(row, 0);

	                try {	                	
	                	if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete?","Confrim",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
	                		TeamMemberController tmc = new TeamMemberController();
							int rs = tmc.delete(tmm, employeeMap.get(employeeName));
							if(rs==1) {
								
								JOptionPane.showMessageDialog(null,"Delete Successfully","Successfully", JOptionPane.INFORMATION_MESSAGE);
								showList();
								clear();
								
							}else {
								JOptionPane.showMessageDialog(null,"Delete fails");
							}
						}
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                    JOptionPane.showMessageDialog(null, "Error while deleting: " + ex.getMessage());
	                }
		        }
			
			}
		});
		
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamMemberModel tmm = new TeamMemberModel();
				TeamMemberController tmc = new TeamMemberController();
				
				// form validation
        		if (cboTeam.getSelectedIndex() == 0 || cboEmployee.getSelectedIndex() == 0 || cboPosition.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
					return;
        		}
        		
				String selectedTeam = (String) cboTeam.getSelectedItem();
				int teamId = teamMap.get(selectedTeam);
				tmm.setTeam_id(teamId);
				
				String selectedEmployee = (String) cboEmployee.getSelectedItem();
				String employeeId = employeeMap.get(selectedEmployee);
				tmm.setEmployee_id(employeeId);
				
				tmm.setPosition((String) cboPosition.getSelectedItem());
				
				   // check duplicate
		        if (tmc.existsTeamMember(teamId, employeeId)) {
		            JOptionPane.showMessageDialog(null, "This employee is already in the selected team!", "Duplicate Entry", JOptionPane.ERROR_MESSAGE);
		            return;
		        }

				try {
					int rs = tmc.insert(tmm);
					if (rs == 1) {
						JOptionPane.showMessageDialog(null, "Save Successfully", "Successfully",
								JOptionPane.INFORMATION_MESSAGE);
//						AutoID();
						showList();
						clear();
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSave.setBounds(29, 85, 89, 23);
		formPanel.add(btnSave);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamMemberModel tmm = new TeamMemberModel();
				TeamMemberController tmc = new TeamMemberController();
				
				// form validation
        		if (cboTeam.getSelectedIndex() == 0 || cboEmployee.getSelectedIndex() == 0 || cboPosition.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
					return;
        		}
        		
				String selectedTeam = (String) cboTeam.getSelectedItem();
				int teamId = teamMap.get(selectedTeam);
				tmm.setTeam_id(teamId);
				
				String selectedEmployee = (String) cboEmployee.getSelectedItem();
				String employeeId = employeeMap.get(selectedEmployee);
				tmm.setEmployee_id(employeeId);
				
				tmm.setPosition((String) cboPosition.getSelectedItem());

				try {
					int rs = tmc.update(tmm, employeeMap.get(employeeName));
					if (rs == 1) {
						JOptionPane.showMessageDialog(null, "Update Successfully", "Successfully",
								JOptionPane.INFORMATION_MESSAGE);
						showList();
						clear();
					}
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnUpdate.setBounds(155, 85, 89, 23);
		formPanel.add(btnUpdate);
		
		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		btnClear.setBounds(281, 85, 89, 23);
		formPanel.add(btnClear);
		
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
		        tableScrollPane.setBounds(padding, tableY, innerWidth, 510);
		    }
		});
		
		createTable();
		showList();
		
		btnUpdate.setEnabled(false);
		
		cboTeamSearch = new JComboBox();
		MySqlQuery.addCoboBox("team", "team_id", "team_name", cboTeamSearch, teamMap);
		cboTeamSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (cboTeamSearch.getSelectedIndex() == 0) {
						showList();
					} else {
						showListOne();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		cboTeamSearch.setBounds(980, 81, 120, 31);
		formPanel.add(cboTeamSearch);
		
		JLabel lblNewLabel_2 = new JLabel("Search");
		lblNewLabel_2.setBounds(920, 89, 46, 14);
		formPanel.add(lblNewLabel_2);
		
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
	    tblTeamMember.getColumnModel().getColumn(3).setCellRenderer(createButtonCellRenderer("Delete"));
	    
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
//		            label.setBackground(new Color(220, 53, 69));
		            label.setForeground(new Color(220, 53, 69));
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
	            setFont(new Font("Arial", Font.BOLD, 14));
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
				data[0] = getNameByEmployeeId(employeeMap, tmm.getEmployee_id());
				data[1] = getNameById(teamMap, tmm.getTeam_id());
				data[2] = tmm.getPosition();
				data[3] = "Delete";
				
//				if ("Project".equals(am.getRelated_type())) {
//					data[2] = getNameById(projectMap, am.getRelated_id());
//			    } else if ("Task".equals(am.getRelated_type())) {
//			        data[2] = getNameById(taskMap, am.getRelated_id());
//			    }

				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showListOne() throws SQLException {
		String data[] = new String[4];
		TeamMemberController tmc = new TeamMemberController();
		TeamMemberModel cm = new TeamMemberModel();
	
		String name=(String) cboTeamSearch.getSelectedItem().toString();
		
		cm.setTeam_id(getIDbyNamev1(teamMap, name));
	
		List<TeamMemberModel> list = tmc.selectone(cm);
		dtm.setRowCount(0);
		for (TeamMemberModel tmm : list) {
			data[0] = getNameByEmployeeId(employeeMap, tmm.getEmployee_id());
			data[1] = getNameById(teamMap, tmm.getTeam_id());
			data[2] = tmm.getPosition();
			data[3] = "Delete";
			dtm.addRow(data);
		}
	}
	
	public static int getIDbyNamev1(Map<String, Integer> map, String sts_name) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getKey().equals(sts_name)) {
				return entry.getValue();
			}
		}
		return 0;
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
	
	public static String getNameByEmployeeId(Map<String, String> map, String id) {
	    for (Map.Entry<String, String> entry : map.entrySet()) {
        	if (entry.getValue().equals(id)) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	public void clear() {
		btnSave.setEnabled(true);
		btnUpdate.setEnabled(false);
		cboEmployee.setSelectedIndex(0);
		cboTeam.setSelectedIndex(0);
		cboPosition.setSelectedIndex(0);
	}
}
