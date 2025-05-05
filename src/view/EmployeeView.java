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

import controller.EmployeeController;
import model.EmployeeModel;


import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;

public class EmployeeView extends JFrame {
	
	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblEmployee;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnClear;
	private JTextField txtEmployeeID;
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField txtPassword;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			EmployeeView empFrame = new EmployeeView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		empFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			empFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			empFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public EmployeeView() {
		setTitle("Employee");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel();
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBounds(10, 0, 661, 293);
		JLabel lblProjectName = new JLabel("EmplolyeeID");
		lblProjectName.setBounds(10, 11, 80, 30);
		txtEmployeeID = new JTextField();
		txtEmployeeID.setBounds(90, 19, 200, 30);

		tblEmployee = new JTable();
		tblEmployee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EmployeeController ec=new EmployeeController();
				int r = tblEmployee.getSelectedRow();
				String Employee_id = (String)tblEmployee.getValueAt(r, 0);
				System.out.println(Employee_id);
				txtEmployeeID.setText(Employee_id);
				txtName.setText((String)tblEmployee.getValueAt(r, 1));
				txtEmail.setText((String)tblEmployee.getValueAt(r, 2));
				try {
					txtPassword.setText(ec.getPswbyId(Employee_id));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				txtName.requestFocus();
				txtName.selectAll();
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(tblEmployee);
		tableScrollPane.setBounds(23, 322, 661, 542);
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblDate = new JLabel("Name");
		lblDate.setBounds(10, 59, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblProjectName);
		formPanel.add(txtEmployeeID);
		formPanel.add(lblDate);

		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setBounds(10, 114, 46, 14);
		formPanel.add(lblNewLabel);

		JLabel lblTeam = new JLabel("Role_id");
		lblTeam.setBounds(347, 25, 46, 14);
		formPanel.add(lblTeam);

		JComboBox cboRoleID = new JComboBox();
		cboRoleID.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));
		cboRoleID.setBounds(420, 23, 200, 30);
		formPanel.add(cboRoleID);

		JLabel lblClient = new JLabel("Passward");
		lblClient.setBounds(10, 161, 46, 14);
		formPanel.add(lblClient);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeModel pm = new EmployeeModel();
				EmployeeController pc = new EmployeeController();
				
				if(txtEmployeeID.getText().trim().toString().equals("")||txtName.getText().trim().toString().equals("") ||
						txtEmail.getText().trim().toString().equals("")||txtPassword.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else {

				pm.setEmployee_id(txtEmployeeID.getText().toString());
				pm.setEmployee_name(txtName.getText().toString());
				pm.setEmail(txtEmail.getText().toString());
				pm.setPassword(txtPassword.getText().toString());

				int statusId = Integer.parseInt(cboRoleID.getSelectedItem().toString());
				pm.setRole_id(statusId);
				if(		Checking.IsValidName(pm.getEmployee_name()) || 
						Checking.IsValidName(pm.getPassword()) || 
						Checking.IsValidName(pm.getEmail()) ||
						Checking.IsValidName(pm.getEmployee_id())
						
						) {
					JOptionPane.showMessageDialog(null, "Invlaid related field","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else if(		
						(Checking.IsAllDigit(pm.getEmployee_name())) || 
						(Checking.IsAllDigit(pm.getPassword())) ||
						(Checking.IsAllDigit(pm.getEmployee_id())) ||
						(Checking.IsAllDigit(pm.getEmail()))
						) {
					JOptionPane.showMessageDialog(null, "All digit Error","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}
				else if(!Checking.IsEmailformat(pm.getEmail())) {
					JOptionPane.showMessageDialog(null, "Email Format Error","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					}
//				else if(!Checking.isPhoneNo(pm.getPhone())) {
//					JOptionPane.showMessageDialog(null, "Phone_number Format Error","Invlaid", JOptionPane.ERROR_MESSAGE);
////					txtShowAll.requestFocus(true);
////					txtShowAll.selectAll();
//				}
				else {

				
				try {
					if (pc.isduplicate(pm)) {
						JOptionPane.showMessageDialog(null, "There is a same project name!", "Fail",
								JOptionPane.ERROR_MESSAGE);
						txtEmployeeID.requestFocus(true);
						txtEmployeeID.selectAll();
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
				EmployeeModel pm = new EmployeeModel();
				EmployeeController pc = new EmployeeController();
				if(txtEmployeeID.getText().trim().toString().equals("")||txtName.getText().trim().toString().equals("") ||
						txtEmail.getText().trim().toString().equals("")||txtPassword.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else {

				pm.setEmployee_id(txtEmployeeID.getText().toString());
				pm.setEmployee_name(txtName.getText().toString());
				pm.setEmail(txtEmail.getText().toString());
				pm.setPassword(txtPassword.getText().toString());

				int statusId = Integer.parseInt(cboRoleID.getSelectedItem().toString());
				pm.setRole_id(statusId);
				
					if(Checking.IsValidName(pm.getEmployee_name()) ) {
						JOptionPane.showMessageDialog(null, "Invlaid name field","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
						txtName.selectAll();
					}else if(Checking.IsValidName(pm.getPassword())) {
						JOptionPane.showMessageDialog(null, "Invlaid email field","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtPassword.requestFocus(true);
						txtPassword.selectAll();
					}else if(Checking.IsAllDigit(pm.getEmployee_name())) {
						JOptionPane.showMessageDialog(null, "Name have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
						txtName.selectAll();
					}else if(Checking.IsAllDigit(pm.getPassword())){
						JOptionPane.showMessageDialog(null, "Password have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtPassword.requestFocus(true);
						txtPassword.selectAll();
					}else if(!Checking.IsEmailformat(pm.getEmail())) {
						JOptionPane.showMessageDialog(null, "Email Format Error","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtEmail.requestFocus(true);
						txtEmail.selectAll();
						}
					
					else {
						try {
							if(pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same supplier name!","Fail", JOptionPane.ERROR_MESSAGE);	
								txtName.requestFocus(true);
								txtName.selectAll();
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
				txtEmployeeID.requestFocus(true);
			}
		});
		btnClear.setBounds(411, 219, 89, 30);
		formPanel.add(btnClear);
		
		txtName = new JTextField();
		txtName.setBounds(90, 67, 200, 30);
		formPanel.add(txtName);
		txtName.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(90, 110, 200, 30);
		formPanel.add(txtEmail);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(91, 154, 200, 30);
		formPanel.add(txtPassword);
		
		rightPanel.add(tableScrollPane);

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);
		
		createTable();
		showList();

	}
	
	public void setColumnWidth(int index , int width)
	{
	     DefaultTableColumnModel tcm = (DefaultTableColumnModel)tblEmployee.getColumnModel();
	     TableColumn tc = tcm.getColumn(index);
	     tc.setPreferredWidth(width);
    }
	
    public void createTable()
	{
	     dtm.addColumn("ID");
	     dtm.addColumn("Name");
	     dtm.addColumn("Email");
	     dtm.addColumn("Status");
	     dtm.addColumn("Role");
	     tblEmployee.setModel(dtm);
	     setColumnWidth(0,60);
	     setColumnWidth(1,60);
	     setColumnWidth(2,150);
	     setColumnWidth(3,100);
	     setColumnWidth(4,100);

	    
	     
    }

	public void clear() {
		btnSave.setEnabled(true);
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);
		txtEmployeeID.setText("");
		txtName.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		

		txtEmployeeID.requestFocus(true);
	}

	public void showList() {
		String data[] = new String[6];
		EmployeeController pc = new EmployeeController();
		try {
			List<EmployeeModel> list = pc.selectall();
			dtm.setRowCount(0);
			for (EmployeeModel pm : list) {
				data[0] = pm.getEmployee_id();
				data[1] = pm.getEmployee_name();
				data[2] = pm.getEmail();
//				data[3] = pm.getPassword();
				data[3] = pm.getStatus();
				data[4] = Integer.toString(pm.getRole_id());
				
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
