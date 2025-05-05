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

import controller.ClientController;
import model.ClientModel;


import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;

public class ClientView extends JFrame {
	
	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblClient;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JButton btnClear;
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField txtCompanyName;
	private JTextField txtCompanyAddress;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ClientView cltFrame = new ClientView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			cltFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			cltFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			cltFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	public ClientView() {
		setTitle("Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel();
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBounds(10, 0, 661, 293);

		tblClient = new JTable();
		tblClient.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ClientController ec=new ClientController();
				int r = tblClient.getSelectedRow();
				String Client_id = (String)tblClient.getValueAt(r, 0);
				System.out.println(Client_id);
				
				txtName.setText((String)tblClient.getValueAt(r, 1));
				txtEmail.setText((String)tblClient.getValueAt(r, 2));
				txtCompanyName.setText((String)tblClient.getValueAt(r, 3));
				txtCompanyAddress.setText((String)tblClient.getValueAt(r, 4));
				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				btnDelete.setEnabled(true);
				txtName.requestFocus();
				txtName.selectAll();
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(tblClient);
		tableScrollPane.setBounds(23, 322, 661, 542);
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblDate = new JLabel("Name");
		lblDate.setBounds(11, 21, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblDate);

		JLabel lblNewLabel = new JLabel("Email");
		lblNewLabel.setBounds(13, 74, 46, 14);
		formPanel.add(lblNewLabel);

		JLabel lblTeam = new JLabel("Company Address");
		lblTeam.setBounds(12, 175, 95, 14);
		formPanel.add(lblTeam);

		JLabel lblClient = new JLabel("Company Name");
		lblClient.setBounds(9, 123, 85, 21);
		formPanel.add(lblClient);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientModel pm = new ClientModel();
				ClientController pc = new ClientController();
				
				if(txtName.getText().trim().toString().equals("") ||
						txtEmail.getText().trim().toString().equals("")||txtCompanyAddress.getText().trim().toString().equals("")||
						txtCompanyName.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else {

				
				pm.setName(txtName.getText().toString());
				pm.setEmail(txtEmail.getText().toString());
				pm.setCompany_name(txtCompanyName.getText().toString());
				pm.setCompany_address(txtCompanyAddress.getText().toString());

				
				if(		Checking.IsValidName(pm.getName()) || 
						Checking.IsValidName(pm.getEmail()) || 
						Checking.IsValidName(pm.getCompany_name()) ||
						Checking.IsValidName(pm.getCompany_address())
						
						) {
					JOptionPane.showMessageDialog(null, "Invlaid related field","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else if(		
						Checking.IsAllDigit(pm.getName()) || 
						Checking.IsAllDigit(pm.getEmail()) ||
						Checking.IsAllDigit(pm.getCompany_name()) ||
						Checking.IsAllDigit(pm.getCompany_address())
						) {
					JOptionPane.showMessageDialog(null, "All digit Error","Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else if(!Checking.IsEmailformat(pm.getEmail())) {
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
						JOptionPane.showMessageDialog(null, "There is a same  name!", "Fail",
								JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
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
				ClientModel pm = new ClientModel();
				ClientController pc = new ClientController();
				if(txtName.getText().trim().toString().equals("") ||
						txtEmail.getText().trim().toString().equals("")||txtCompanyName.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "There is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				}else {
//
//				try {
//					pm.setClient_id(pc.getIDbyName(txtName.getText()));
//				} catch (SQLException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				}
					int r = tblClient.getSelectedRow();
					int Client_id = Integer.parseInt(tblClient.getValueAt(r, 0).toString());

				pm.setClient_id(Client_id);
				pm.setName(txtName.getText().toString());
				pm.setEmail(txtEmail.getText().toString());
				pm.setCompany_name(txtCompanyName.getText().toString());
				pm.setCompany_address(txtCompanyAddress.getText().toString());

//				int statusId = Integer.parseInt(cboRoleID.getSelectedItem().toString());
//				pm.setRole_id(statusId);
				
					if(Checking.IsValidName(pm.getName()) ) {
						JOptionPane.showMessageDialog(null, "Invlaid name field","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
						txtName.selectAll();
					}else if(Checking.IsValidName(pm.getEmail())) {
						JOptionPane.showMessageDialog(null, "Invlaid email field","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtCompanyName.requestFocus(true);
						txtCompanyName.selectAll();
					}else if(Checking.IsAllDigit(pm.getCompany_name())) {
						JOptionPane.showMessageDialog(null, "Company Name have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
						txtName.selectAll();
					}else if(Checking.IsAllDigit(pm.getCompany_address())){
						JOptionPane.showMessageDialog(null, "Company Address have all digit","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtCompanyName.requestFocus(true);
						txtCompanyName.selectAll();
					}else if(!Checking.IsEmailformat(pm.getEmail())) {
						JOptionPane.showMessageDialog(null, "Email Format Error","Invlaid", JOptionPane.ERROR_MESSAGE);
						txtEmail.requestFocus(true);
						txtEmail.selectAll();
						}
					
					else {
						try {
							if(pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same Customer name!","Fail", JOptionPane.ERROR_MESSAGE);	
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
							}
						catch (HeadlessException | SQLException e1) {
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
				txtName.requestFocus(true);
			}
		});
		btnClear.setBounds(411, 219, 89, 30);
		formPanel.add(btnClear);
		
		txtName = new JTextField();
		txtName.setBounds(116, 21, 200, 30);
		formPanel.add(txtName);
		txtName.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(116, 67, 200, 30);
		formPanel.add(txtEmail);
		
		txtCompanyName = new JTextField();
		txtCompanyName.setColumns(10);
		txtCompanyName.setBounds(116, 118, 200, 30);
		formPanel.add(txtCompanyName);
		
		txtCompanyAddress = new JTextField();
		txtCompanyAddress.setBounds(117, 169, 200, 30);
		formPanel.add(txtCompanyAddress);
		
		rightPanel.add(tableScrollPane);

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);
		
		createTable();
		showList();

	}
	
	public void setColumnWidth(int index , int width)
	{
	     DefaultTableColumnModel tcm = (DefaultTableColumnModel)tblClient.getColumnModel();
	     TableColumn tc = tcm.getColumn(index);
	     tc.setPreferredWidth(width);
    }
	
    public void createTable()
	{
	     dtm.addColumn("ID");
	     dtm.addColumn("Name");
	     dtm.addColumn("Email");
	     dtm.addColumn("Company Name");
	     dtm.addColumn("Company Address");
	     tblClient.setModel(dtm);
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
		txtName.setText("");
		txtEmail.setText("");
		txtCompanyName.setText("");
		txtCompanyAddress.setText("");
//		txtName.setText("");
		
		

		txtName.requestFocus(true);
	}

	public void showList() {
		String data[] = new String[6];
		ClientController pc = new ClientController();
		try {
			List<ClientModel> list = pc.selectall();
			dtm.setRowCount(0);
			for (ClientModel pm : list) {
				data[0] = Integer.toString(pm.getClient_id());
				data[1] = pm.getName();
				data[2] = pm.getEmail();
				data[3] = pm.getCompany_name();
				data[4] = pm.getCompany_address();
				
				
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
