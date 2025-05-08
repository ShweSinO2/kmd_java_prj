package view;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;

import config.Checking;

import controller.TeamController;
import model.TeamModel;


import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;

public class TeamView extends JFrame {
	
	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblTeam;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnClear;
	private JTextField txtTeamName;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			TeamView cltFrame = new TeamView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			cltFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			cltFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			cltFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public TeamView() {
		setTitle("Team");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel("TeamView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBounds(10, 0, 661, 293);

		tblTeam = new JTable();
		tblTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TeamController ec=new TeamController();
				int r = tblTeam.getSelectedRow();
				String Team_id = (String)tblTeam.getValueAt(r, 0);
				System.out.println(Team_id);
				
				txtTeamName.setText((String)tblTeam.getValueAt(r, 1));
				

				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				txtTeamName.requestFocus();
				txtTeamName.selectAll();
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(tblTeam);
		tableScrollPane.setBounds(23, 322, 661, 542);
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblTeamName = new JLabel("Team Name");
		lblTeamName.setBounds(11, 21, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblTeamName);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamModel pm = new TeamModel();
				TeamController pc = new TeamController();
				
				if(txtTeamName.getText().trim().toString().equals("") ) {
					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else {

				
				pm.setTeam_name(txtTeamName.getText().toString());
				

				
				if(		Checking.IsValidName(pm.getTeam_name()) ) {
					JOptionPane.showMessageDialog(null, "Invlaid related field","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else if(		
						Checking.IsAllDigit(pm.getTeam_name()) ) {
					JOptionPane.showMessageDialog(null, "All digit Error","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}
				else {

				
				try {
					if (pc.isduplicate(pm)) {
						JOptionPane.showMessageDialog(null, "There is a same  name!", "Fail",
								JOptionPane.ERROR_MESSAGE);
						txtTeamName.requestFocus(true);
//						txtName.selectAll();
					} else {
						int rs = pc.insert(pm);
						if (rs == 1) {
							JOptionPane.showMessageDialog(null, "Save Successfully", "Successfully",
									JOptionPane.INFORMATION_MESSAGE);
//							AutoID();
							showList();
							clear();
						}

					}
				} catch (HeadlessException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				
				
				}
			}
		});
		btnSave.setBounds(35, 219, 89, 30);
		formPanel.add(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamModel pm = new TeamModel();
				TeamController pc = new TeamController();
				if(txtTeamName.getText().trim().toString().equals("") ) {
					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else {

				int r = tblTeam.getSelectedRow();
					int Client_id = Integer.parseInt(tblTeam.getValueAt(r, 0).toString());
				pm.setTeam_id(Client_id);
				pm.setTeam_name(txtTeamName.getText().toString());
				

//				int statusId = Integer.parseInt(cboRoleID.getSelectedItem().toString());
//				pm.setRole_id(statusId);
				
					if(Checking.IsValidName(pm.getTeam_name()) ) {
						JOptionPane.showMessageDialog(null, "Invlaid name field","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtTeamName.requestFocus(true);
						txtTeamName.selectAll();
					}else if(Checking.IsAllDigit(pm.getTeam_name())) {
						JOptionPane.showMessageDialog(null, "Team Name have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtTeamName.requestFocus(true);
						txtTeamName.selectAll();
					}
					
					else {
						try {
							if(pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same Team name!","Fail", JOptionPane.ERROR_MESSAGE);	
								txtTeamName.requestFocus(true);
								txtTeamName.selectAll();
							}else {
								int rs = pc.update(pm);
								if(rs==1) {
									JOptionPane.showMessageDialog(null, "Update Successfully","Successfully", JOptionPane.INFORMATION_MESSAGE);
									
									clear();
									showList();
								}
								
							}
						} catch (HeadlessException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnUpdate.setBounds(158, 219, 89, 30);
		formPanel.add(btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setBounds(280, 219, 89, 30);
		formPanel.add(btnDelete);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				txtTeamName.requestFocus(true);
			}
		});
		btnClear.setBounds(411, 219, 89, 30);
		formPanel.add(btnClear);
		
		txtTeamName = new JTextField();
		txtTeamName.setBounds(116, 21, 200, 30);
		formPanel.add(txtTeamName);
		txtTeamName.setColumns(10);
		
		rightPanel.add(tableScrollPane);

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);
		
		createTable();
		showList();

	}
	
	public void setColumnWidth(int index , int width)
	{
	     DefaultTableColumnModel tcm = (DefaultTableColumnModel)tblTeam.getColumnModel();
	     TableColumn tc = tcm.getColumn(index);
	     tc.setPreferredWidth(width);
    }
	
    public void createTable()
	{
	     dtm.addColumn("Team ID");
	     dtm.addColumn("Team Name");
	    
	     tblTeam.setModel(dtm);
	     setColumnWidth(0,60);
	     setColumnWidth(1,60);
	     

	    
	     
    }

	public void clear() {
		btnSave.setEnabled(true);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		txtTeamName.setText("");
		
//		txtName.setText("");
		
		

		txtTeamName.requestFocus(true);
	}

	public void showList() {
		String data[] = new String[6];
		TeamController pc = new TeamController();
		try {
			List<TeamModel> list = pc.selectall();
			dtm.setRowCount(0);
			for (TeamModel pm : list) {
				data[0] = Integer.toString(pm.getTeam_id());
				data[1] = pm.getTeam_name();
				
				
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
