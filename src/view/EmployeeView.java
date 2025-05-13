package view;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;

import config.Checking;
import config.MySqlQuery;
import controller.EmployeeController;
import controller.ProjectController;
import model.EmployeeModel;
import model.ProjectModel;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;

public class EmployeeView extends JFrame {

	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblEmployee;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnClear;
	private JTextField txtEmployeeID;
	private JTextField txtName;
	private JTextField txtEmail;
	private JTextField txtPassword;
	private JComboBox cboRoleID;
	Map<String, Integer> roleMap = new HashMap<>();
	private JTextField txtPhone;
	private JTextField txtShowAll;
	private JLabel lblSearch;

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

		SideMenuPanel sideMenu = new SideMenuPanel("EmployeeView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBackground(new Color(255, 255, 255));
		formPanel.setBounds(10, 0, 661, 293);

		JLabel lblProjectName = new JLabel("EmplolyeeID");
		lblProjectName.setBounds(10, 11, 80, 30);
		txtEmployeeID = new JTextField();
		txtEmployeeID.setBounds(100, 11, 200, 30);

		tblEmployee = new JTable();
		tblEmployee.setRowHeight(20);
		tblEmployee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				EmployeeController ec = new EmployeeController();
				int r = tblEmployee.getSelectedRow();
				int column = tblEmployee.columnAtPoint(e.getPoint());
				EmployeeModel pm = new EmployeeModel();
				String Employee_id = (String) tblEmployee.getValueAt(r, 0);
				
				txtEmployeeID.setText(Employee_id);
				txtName.setText((String) tblEmployee.getValueAt(r, 1));
				txtPhone.setText((String) tblEmployee.getValueAt(r, 2));
				txtEmail.setText((String) tblEmployee.getValueAt(r, 3));

				String statusName = (String) tblEmployee.getValueAt(r, 4);
				cboRoleID.setSelectedItem(statusName);
				try {
					txtPassword.setText(ec.getPswbyId(Employee_id));
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				txtName.requestFocus();
				txtName.selectAll();
				if (column == 5) {
					DefaultTableModel model = (DefaultTableModel) tblEmployee.getModel();
					String employeeId = (String) model.getValueAt(r, 0);
					pm.setEmployee_id(employeeId);

					try {
						if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confrim",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							EmployeeController pc = new EmployeeController();
							int rs = pc.delete(pm);
							if (rs == 1) {

								JOptionPane.showMessageDialog(null, "Delete Successfully", "Successfully",
										JOptionPane.INFORMATION_MESSAGE);
//								AutoID();
								showList();
								clear();

							} else {
								System.out.println(rs);
								JOptionPane.showMessageDialog(null, "Delete fails");
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Error while deleting: " + ex.getMessage());
					}
				}
			}
		});
		JScrollPane tableScrollPane = new JScrollPane(tblEmployee);
		tableScrollPane.setBounds(23, 322, 661, 542);
		tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
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
		lblNewLabel.setBounds(10, 124, 46, 14);
		formPanel.add(lblNewLabel);

		JLabel lblTeam = new JLabel("Role_id");
		lblTeam.setBounds(347, 19, 46, 14);
		formPanel.add(lblTeam);

		cboRoleID = new JComboBox();
		MySqlQuery.addCoboBox("role", "role_id", "role_name", cboRoleID, roleMap);
		cboRoleID.setBounds(438, 11, 200, 30);
		formPanel.add(cboRoleID);

		JLabel lblClient = new JLabel("Password");
		lblClient.setBounds(347, 116, 80, 14);
		formPanel.add(lblClient);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeModel pm = new EmployeeModel();
				EmployeeController pc = new EmployeeController();

				if (txtEmployeeID.getText().trim().toString().equals("")
						|| txtName.getText().trim().toString().equals("")
						|| txtEmail.getText().trim().toString().equals("")
						|| txtPassword.getText().trim().toString().equals("")
						|| cboRoleID.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else {

					pm.setEmployee_id(txtEmployeeID.getText().toString());
					pm.setEmployee_name(txtName.getText().toString());
					pm.setPhone(txtPhone.getText().toString());
					pm.setEmail(txtEmail.getText().toString());
					pm.setPassword(txtPassword.getText().toString());

					String selectedRole = (String) cboRoleID.getSelectedItem();
					int roleId = roleMap.get(selectedRole);
					pm.setRole_id(roleId);
//				int statusId = Integer.parseInt(cboRoleID.getSelectedItem().toString());
//				pm.setRole_id(statusId);
					if (Checking.IsValidName(pm.getEmployee_name()) || Checking.IsValidName(pm.getPassword())
							|| Checking.IsValidName(pm.getPhone()) || Checking.IsValidName(pm.getEmail())
							|| cboRoleID.getSelectedIndex() == 0 || Checking.IsValidName(pm.getEmployee_id())

					) {
						JOptionPane.showMessageDialog(null, "Invlaid related field", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					} else if ((Checking.IsAllDigit(pm.getEmployee_name())) || (Checking.IsAllDigit(pm.getPassword()))
							|| (Checking.IsAllDigit(pm.getEmployee_id())) || (Checking.IsAllDigit(pm.getEmail()))) {
						JOptionPane.showMessageDialog(null, "All digit Error", "Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					} else if (!Checking.IsEmailformat(pm.getEmail())) {
						JOptionPane.showMessageDialog(null, "Email Format Error", "Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					} else if (!Checking.isPhoneNo(pm.getPhone())) {
						JOptionPane.showMessageDialog(null, "Phone_number Format Error", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					} else {

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
		btnSave.setBounds(20, 175, 89, 30);
		formPanel.add(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeModel pm = new EmployeeModel();
				EmployeeController pc = new EmployeeController();
				if (txtEmployeeID.getText().trim().toString().equals("")
						|| txtName.getText().trim().toString().equals("")
						|| txtPhone.getText().trim().toString().equals("")
						|| txtEmail.getText().trim().toString().equals("")
						|| txtPassword.getText().trim().toString().equals("")
						|| cboRoleID.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else {

					pm.setEmployee_id(txtEmployeeID.getText().toString());
					pm.setEmployee_name(txtName.getText().toString());
					pm.setPhone(txtPhone.getText().toString());
					pm.setEmail(txtEmail.getText().toString());
					pm.setPassword(txtPassword.getText().toString());

					String selectedRole = (String) cboRoleID.getSelectedItem();
					int roleId = roleMap.get(selectedRole);
					pm.setRole_id(roleId);

					if (Checking.IsValidName(pm.getEmployee_name())) {
						JOptionPane.showMessageDialog(null, "Invlaid name field", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
						txtName.selectAll();
					} else if (!Checking.IsEmailformat(pm.getEmail())) {
						JOptionPane.showMessageDialog(null, "Email Format Error", "Invlaid", JOptionPane.ERROR_MESSAGE);
//						txtShowAll.requestFocus(true);
//						txtShowAll.selectAll();
					} else if (Checking.IsValidName(pm.getPassword())) {
						JOptionPane.showMessageDialog(null, "Invlaid email field", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtPassword.requestFocus(true);
						txtPassword.selectAll();
					} else if (Checking.IsAllDigit(pm.getEmployee_name())) {
						JOptionPane.showMessageDialog(null, "Name have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtName.requestFocus(true);
						txtName.selectAll();
					} else if (Checking.IsAllDigit(pm.getPassword())) {
						JOptionPane.showMessageDialog(null, "Password have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtPassword.requestFocus(true);
						txtPassword.selectAll();
					} else if (!Checking.IsEmailformat(pm.getEmail())) {
						JOptionPane.showMessageDialog(null, "Email Format Error", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtEmail.requestFocus(true);
						txtEmail.selectAll();
					}

					else {
						try {
							if (pc.isduplicate1(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same employee name!", "Fail",
										JOptionPane.ERROR_MESSAGE);
								txtName.requestFocus(true);
								txtName.selectAll();
							} else {
								int rs = pc.update(pm);
								if (rs == 1) {
									JOptionPane.showMessageDialog(null, "Update Successfully", "Successfully",
											JOptionPane.INFORMATION_MESSAGE);

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
		btnUpdate.setBounds(142, 175, 89, 30);
		formPanel.add(btnUpdate);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				txtEmployeeID.requestFocus(true);
			}
		});
		btnClear.setBounds(261, 175, 89, 30);
		formPanel.add(btnClear);

		txtName = new JTextField();
		txtName.setBounds(100, 59, 200, 30);
		formPanel.add(txtName);
		txtName.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(100, 112, 200, 30);
		formPanel.add(txtEmail);

		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(438, 108, 200, 30);
		formPanel.add(txtPassword);

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setBounds(347, 59, 80, 30);
		formPanel.add(lblPhone);

		txtPhone = new JTextField();
		txtPhone.setColumns(10);
		txtPhone.setBounds(438, 59, 200, 30);
		formPanel.add(txtPhone);

		rightPanel.add(tableScrollPane);

		// Update component bounds on resize
		rightPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int padding = 16;
				int formHeight = 240;
				int formMarginBottom = 16;

				int width = rightPanel.getWidth();
				int height = rightPanel.getHeight();

				int innerWidth = width - (padding * 2);
				int innerHeight = height - (padding * 2);

				int tableY = padding + formHeight + formMarginBottom;
				int tableHeight = innerHeight - formHeight - formMarginBottom;

				formPanel.setBounds(padding, padding, innerWidth, formHeight);
				tableScrollPane.setBounds(padding, tableY, innerWidth, 420);
			}
		});

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);

		createTable();
		showList();

		btnUpdate.setEnabled(false);

		txtShowAll = new JTextField();
		txtShowAll.setColumns(10);
		txtShowAll.setBounds(890, 175, 200, 30);
		formPanel.add(txtShowAll);

		lblSearch = new JLabel("Search");
		lblSearch.setBounds(830, 183, 80, 14);
		formPanel.add(lblSearch);
		
		JButton btnCsvexport = new JButton("ExportToCSV");
		btnCsvexport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmployeeController ec = new EmployeeController();
				List<EmployeeModel> employeeList;
				try {
					employeeList = ec.selectall();
					exportToCSV(employeeList);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCsvexport.setBounds(377, 175, 124, 30);
		formPanel.add(btnCsvexport);

		txtShowAll.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					if (txtShowAll.getText().toString().trim().equals("")) {
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

	}

	public void setColumnWidth(int index, int width) {
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) tblEmployee.getColumnModel();
		TableColumn tc = tcm.getColumn(index);
		tc.setPreferredWidth(width);
	}

	public void createTable() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Phone");
		dtm.addColumn("Email");
//		dtm.addColumn("Status");
		dtm.addColumn("Role");
		dtm.addColumn("");
		tblEmployee.setModel(dtm);
		tblEmployee.setRowHeight(25);
		setColumnWidth(0, 60);
		setColumnWidth(1, 60);
		setColumnWidth(2, 150);
		setColumnWidth(3, 100);
		setColumnWidth(4, 100);
		setColumnWidth(5, 100);
//		setColumnWidth(6, 100);

		// Customize table header
		JTableHeader header = tblEmployee.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Set header height

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment

		// Apply center alignment to specific columns
		tblEmployee.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tblEmployee.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		tblEmployee.getColumnModel().getColumn(5).setCellRenderer(createButtonCellRenderer("Delete"));

		// Set custom header renderer
		DefaultTableCellRenderer headerRenderer = createHeaderRenderer();

		// Apply header renderer to all columns
		for (int i = 0; i < tblEmployee.getColumnModel().getColumnCount(); i++) {
			tblEmployee.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
		}
	}

	private DefaultTableCellRenderer createButtonCellRenderer(final String buttonText) {
		return new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {

				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setHorizontalAlignment(SwingConstants.CENTER);

				JLabel label = new JLabel(buttonText);
				label.setHorizontalAlignment(SwingConstants.CENTER); // Center text

				// Style the label like a button
				label.setPreferredSize(new Dimension(30, 15));
				label.setBackground(new Color(255, 255, 255));
				label.setForeground(new Color(40, 167, 69));
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));

				// download design
				if (buttonText == "Delete") {
					label.setForeground(new Color(220, 53, 69));
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
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
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

	public void clear() {
		btnSave.setEnabled(true);
		btnUpdate.setEnabled(false);
		txtEmployeeID.setText("");
		txtName.setText("");
		txtPhone.setText("");
		txtEmail.setText("");
		txtPassword.setText("");
		cboRoleID.setSelectedIndex(0);

		txtEmployeeID.requestFocus(true);
	}

	public void showListOne() throws SQLException {
		String data[] = new String[6];
		EmployeeController cc = new EmployeeController();
		EmployeeModel cm = new EmployeeModel();
		cm.setEmployee_name(txtShowAll.getText().toString().trim());
		List<EmployeeModel> list = cc.selectone(cm);
		dtm.setRowCount(0);
		for (EmployeeModel c : list) {
			data[0] = c.getEmployee_id();
			data[1] = c.getEmployee_name();
			data[2] = c.getPhone();
			data[3] = c.getEmail();
//			data[3] = pm.getPassword();
//			data[4] = c.getStatus();
			data[4] = getNameById(roleMap, c.getRole_id());
			data[5] = "Delete";
			dtm.addRow(data);
		}
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
				data[2] = pm.getPhone();
				data[3] = pm.getEmail();
//				data[3] = pm.getPassword();
//				data[4] = pm.getStatus();
				data[4] = getNameById(roleMap, pm.getRole_id());
				data[5] = "Delete";
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void exportToCSV(List<EmployeeModel> employeeList) {
		MySqlQuery.getComboData("role", "role_id", "role_name", roleMap);
		
		String userHome = System.getProperty("user.home");
		String timestamp = String.valueOf(System.currentTimeMillis());
		String downloadsPath = userHome + "\\Downloads\\" + timestamp +"_employee.csv";

		try (FileWriter writer = new FileWriter(downloadsPath)) {
			// Write CSV header
			writer.append("EmployeeID,Name,Phone,Email,Role\n");

			// Write project data
			for (EmployeeModel em : employeeList) {
				writer.append(String.valueOf(em.getEmployee_id())).append(",");
				writer.append(escapeCsv(em.getEmployee_name())).append(",");
				writer.append("=\"").append(em.getPhone()).append("\"").append(",");
				writer.append(String.valueOf(em.getEmail())).append(",");
				writer.append(String.valueOf(getNameById(roleMap, em.getRole_id()))).append("\n");
			}

			 JOptionPane.showMessageDialog(null, "CSV Export Successful!",
		                "Export Successful", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Helper method to handle commas and quotes in CSV
	private String escapeCsv(String value) {
		if (value == null)
			return "";
		if (value.contains(",") || value.contains("\"")) {
			value = value.replace("\"", "\"\"");
			return "\"" + value + "\"";
		}
		return value;
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
