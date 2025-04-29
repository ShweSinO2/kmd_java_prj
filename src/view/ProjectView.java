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

import controller.ProjectController;
import model.ProjectModel;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProjectView extends JFrame {
	DefaultTableModel dtm = new DefaultTableModel();
	private JTextField txtDescription;
	private JTable tblProject;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnClear;
	private JTextField txtProjectName;
	private JDateChooser startDate;
	private JDateChooser endDate;
	String Project_id = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ProjectView prjFrame = new ProjectView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			prjFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			prjFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			prjFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ProjectView() {
		setTitle("Projects");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel();
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBounds(10, 0, 661, 293);
		formPanel.setBorder(new LineBorder(Color.GREEN, 4, true));
		JLabel lblProjectName = new JLabel("Name:");
		lblProjectName.setBounds(10, 11, 80, 30);
		txtProjectName = new JTextField();
		txtProjectName.setBounds(91, 11, 200, 30);

		tblProject = new JTable();
		tblProject.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblProject.rowAtPoint(e.getPoint());
				ProjectModel pm = new ProjectModel();

				//update row for project
				System.out.println("------");
				Project_id = (String)tblProject.getValueAt(row, 0);
				pm.setProject_id(Integer.parseInt(Project_id));
				System.out.println(Project_id);
				txtProjectName.setText((String)tblProject.getValueAt(row, 1));
				txtDescription.setText((String)tblProject.getValueAt(row, 2));
			    
			    try {
			        String startDateStr = (String) tblProject.getValueAt(row, 3);
			        String endDateStr = (String) tblProject.getValueAt(row, 4);
			        
			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			        Date start = sdf.parse(startDateStr);
			        Date end = sdf.parse(endDateStr);
			        
			        startDate.setDate(start); // JDateChooser method
			        endDate.setDate(end);     // JDateChooser method
			    } catch (ParseException e1) {
			        e1.printStackTrace();
			    }

				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				txtProjectName.requestFocus();
				
				//delete row for project
		        int column = tblProject.columnAtPoint(e.getPoint());
		        if (column == 8) {
		        	DefaultTableModel model = (DefaultTableModel) tblProject.getModel();
	                String projectIdStr = (String) model.getValueAt(row, 0);
	                int projectId = Integer.parseInt(projectIdStr);

	                try {	                	
	                	if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete?","Confrim",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
	                		ProjectController pc = new ProjectController();
							int rs = pc.delete(pm);
							if(rs==1) {
								
								JOptionPane.showMessageDialog(null,"Delete Successfully","Successfully", JOptionPane.INFORMATION_MESSAGE);
//								AutoID();
								showList();
								clear();
								
							}else {
								System.out.println(rs);
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
		JScrollPane tableScrollPane = new JScrollPane(tblProject);
		tableScrollPane.setBounds(23, 322, 661, 542);
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblDescription = new JLabel("Description:");
		lblDescription.setBounds(353, 11, 80, 30);

		txtDescription = new JTextField();
		txtDescription.setBounds(434, 11, 200, 30);

		JLabel lblDate = new JLabel("StartDate");
		lblDate.setBounds(10, 59, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblProjectName);
		formPanel.add(txtProjectName);
		formPanel.add(lblDescription);
		formPanel.add(txtDescription);
		formPanel.add(lblDate);

		startDate = new JDateChooser();
		startDate.setBounds(91, 63, 201, 30);
		startDate.setDateFormatString("yyyy-MM-dd");
		formPanel.add(startDate);

		JLabel lblEnddate = new JLabel("EndDate");
		lblEnddate.setBounds(352, 59, 80, 30);
		formPanel.add(lblEnddate);

		endDate = new JDateChooser();
		endDate.setBounds(434, 63, 200, 30);
		endDate.setDateFormatString("yyyy-MM-dd");
		formPanel.add(endDate);

		JLabel lblNewLabel = new JLabel("Status");
		lblNewLabel.setBounds(10, 114, 46, 14);
		formPanel.add(lblNewLabel);

		JComboBox cboStatus = new JComboBox();
		cboStatus.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
		cboStatus.setBounds(91, 110, 200, 30);
		formPanel.add(cboStatus);

		JLabel lblTeam = new JLabel("Team");
		lblTeam.setBounds(353, 114, 46, 14);
		formPanel.add(lblTeam);

		JComboBox cboTeam = new JComboBox();
		cboTeam.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
		cboTeam.setBounds(434, 110, 200, 30);
		formPanel.add(cboTeam);

		JLabel lblClient = new JLabel("Client");
		lblClient.setBounds(10, 161, 46, 14);
		formPanel.add(lblClient);

		JComboBox cboClient = new JComboBox();
		cboClient.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
		cboClient.setBounds(91, 157, 200, 30);
		formPanel.add(cboClient);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectModel pm = new ProjectModel();
				ProjectController pc = new ProjectController();
				// to write form validation
//        		if(){
//        			
//        		}else {
//        			
//        		}
				pm.setProject_name(txtProjectName.getText().toString());
				pm.setDescription(txtDescription.getText().toString());

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String formattedStartDate = sdf.format(startDate.getDate());
				pm.setStart_date(formattedStartDate);

				String formattedEndDate = sdf.format(endDate.getDate());
				pm.setEnd_date(formattedEndDate);

				int statusId = Integer.parseInt(cboStatus.getSelectedItem().toString());
				pm.setStatus_id(statusId);

				int teamId = Integer.parseInt(cboTeam.getSelectedItem().toString());
				pm.setTeam_id(statusId);

				int clientId = Integer.parseInt(cboClient.getSelectedItem().toString());
				pm.setClient_id(statusId);

				try {
					if (pc.isduplicate(pm)) {
						JOptionPane.showMessageDialog(null, "There is a same project name!", "Fail",
								JOptionPane.ERROR_MESSAGE);
						txtProjectName.requestFocus(true);
						txtProjectName.selectAll();
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
		});
		btnSave.setBounds(35, 219, 89, 30);
		formPanel.add(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectModel pm = new ProjectModel();
				ProjectController pc = new ProjectController();
				// to write form validation
//        		if(){
//        			
//        		}else {
//        			
//        		}
				pm.setProject_id(Integer.parseInt(Project_id));
				System.out.println("--------");
				System.out.println(pm.getProject_id());
				pm.setProject_name(txtProjectName.getText().toString());
				pm.setDescription(txtDescription.getText().toString());

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String formattedStartDate = sdf.format(startDate.getDate());
				pm.setStart_date(formattedStartDate);

				String formattedEndDate = sdf.format(endDate.getDate());
				pm.setEnd_date(formattedEndDate);

				int statusId = Integer.parseInt(cboStatus.getSelectedItem().toString());
				pm.setStatus_id(statusId);

				int teamId = Integer.parseInt(cboTeam.getSelectedItem().toString());
				pm.setTeam_id(statusId);

				int clientId = Integer.parseInt(cboClient.getSelectedItem().toString());
				pm.setClient_id(statusId);
				
				try {
					if(pc.isduplicate(pm)) {
						JOptionPane.showMessageDialog(null, "There is a same supplier name!","Fail", JOptionPane.ERROR_MESSAGE);	
						txtProjectName.requestFocus(true);
//						txtCustomerName.selectAll();
					}else {
						int rs = pc.update(pm);
						if(rs==1) {
							JOptionPane.showMessageDialog(null, "Update Successfully","Successfully", JOptionPane.INFORMATION_MESSAGE);
//							AutoID();
							clear();
							showList();
						}
						
					}
				}catch (HeadlessException | SQLException e1) {
//					// TODO Auto-generated catch block
					e1.printStackTrace();
					
				}
//				if(lblCustomerID.getText().trim().toString().equals("")||txtCustomerName.getText().trim().toString().equals("") ||
//						txtCustomerAddress.getText().trim().toString().equals("")||txtCustomerPhone.getText().trim().toString().equals("")||txtCustomerAddress.getText().trim().toString().equals("")) {
//					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtCustomerName.requestFocus(true);
//					txtCustomerName.selectAll();
//				}else {
//					cm.setCustomer_id(lblCustomerID.getText().toString());
//					cm.setName(txtCustomerName.getText().toString());
//					cm.setAddress(txtCustomerAddress.getText().toString());
//					cm.setEmail(txtCustomerEmail.getText().toString());
//					cm.setPhone(txtCustomerPhone.getText().toString());
//					if(Checking.IsValidName(cm.getName()) ) {
//						JOptionPane.showMessageDialog(null, "Invlaid name field","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}else if(Checking.IsValidName(cm.getAddress())) {
//						JOptionPane.showMessageDialog(null, "Invlaid address field","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}else if(Checking.IsValidName(cm.getEmail())) {
//						JOptionPane.showMessageDialog(null, "Invlaid email field","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}else if(Checking.IsValidName(cm.getPhone())) {
//						JOptionPane.showMessageDialog(null, "Invlaid phone field","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}else if(!Checking.IsAllDigit(cm.getName())) {
//						JOptionPane.showMessageDialog(null, "Name have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}else if(!Checking.IsAllDigit(cm.getAddress())){
//						JOptionPane.showMessageDialog(null, "Address have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}else if(!Checking.IsEmailformat(cm.getEmail())) {
//						JOptionPane.showMessageDialog(null, "Email Format Error","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//						}
//					else if(!Checking.isPhoneNo(cm.getPhone())) {
//						JOptionPane.showMessageDialog(null, "Phone_number Format Error","Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtCustomerName.requestFocus(true);
//						txtCustomerName.selectAll();
//					}
//					else {
//						try {
//							if(cc.isduplicate(cm)) {
//								JOptionPane.showMessageDialog(null, "There is a same supplier name!","Fail", JOptionPane.ERROR_MESSAGE);	
//								txtCustomerName.requestFocus(true);
//								txtCustomerName.selectAll();
//							}else {
//								int rs = cc.update(cm);
//								if(rs==1) {
//									JOptionPane.showMessageDialog(null, "Update Successfully","Successfully", JOptionPane.INFORMATION_MESSAGE);
//									AutoID();
//									clear();
//									showList();
//								}
//								
//							}
//						} catch (HeadlessException | SQLException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
//				}
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
				txtProjectName.requestFocus(true);
			}
		});
		btnClear.setBounds(411, 219, 89, 30);
		formPanel.add(btnClear);
		rightPanel.add(tableScrollPane);

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);
		
		createTable();
		showList();

	}
	
	public void setColumnWidth(int index , int width)
	{
	     DefaultTableColumnModel tcm = (DefaultTableColumnModel)tblProject.getColumnModel();
	     TableColumn tc = tcm.getColumn(index);
	     tc.setPreferredWidth(width);
    }
	
    public void createTable()
	{
	     dtm.addColumn("ID");
	     dtm.addColumn("Name");
	     dtm.addColumn("Description"); 
	     dtm.addColumn("StartDate");
	     dtm.addColumn("EndDate");
	     dtm.addColumn("Status");
	     dtm.addColumn("Team");
	     dtm.addColumn("Client");
	     dtm.addColumn("Action");
	     tblProject.setModel(dtm);
	     setColumnWidth(0,60);
	     setColumnWidth(1,60);
	     setColumnWidth(2,150);
	     setColumnWidth(3,100);
	     setColumnWidth(4,100);
	     setColumnWidth(5,100);
	     setColumnWidth(6,100);
	     setColumnWidth(7,100);
	     setColumnWidth(8,100);
	     
    }

	public void clear() {
		btnSave.setEnabled(true);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		txtProjectName.setText("");
		txtDescription.setText("");
		startDate.setDate(null);
		endDate.setDate(null);

		txtProjectName.requestFocus(true);
	}

	public void showList() {
		String data[] = new String[9];
		ProjectController pc = new ProjectController();
		try {
			List<ProjectModel> list = pc.selectall();
			dtm.setRowCount(0);
			for (ProjectModel pm : list) {
				data[0] = Integer.toString(pm.getProject_id());;
				data[1] = pm.getProject_name();
				data[2] = pm.getDescription();
				data[3] = pm.getStart_date();
				data[4] = pm.getEnd_date();
				data[5] = Integer.toString(pm.getStatus_id());
				data[6] = Integer.toString(pm.getTeam_id());
				data[7] = Integer.toString(pm.getClient_id());
				data[8] = "Delete";
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
