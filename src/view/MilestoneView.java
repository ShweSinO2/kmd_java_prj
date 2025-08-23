package view;

import javax.swing.*;
import java.awt.*;

import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import com.toedter.calendar.JDateChooser;

import config.Checking;
import config.MySqlQuery;
import controller.MilestoneController;
import model.MilestoneModel;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MilestoneView extends JFrame {

	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblMilestone;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnClear;
	private JTextField txtMilestoneName;
	private JDateChooser txtDueDate;
	private JComboBox cboStatus;
	private JComboBox cboProject;
	String Project_id = null;
	Map<String, Integer> statusMap = new HashMap<>();
	Map<String, Integer> projectMap = new HashMap<>();
	private JTextField txtShowAll;
	private JLabel lblSearch;
	String username,password;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			MilestoneView mstFrame = new MilestoneView();
//			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			mstFrame.setBounds(0, 0, screenSize.width, screenSize.height);
//			mstFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			mstFrame.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public MilestoneView(String username,String password) {
		this.username = username;
		this.password = password;
		
		setTitle("Milestone");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel(username,password,"MilestoneView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBackground(new Color(255, 255, 255));
		formPanel.setBounds(10, 0, 661, 293);

		JLabel lblProjectName = new JLabel("Name:");
		lblProjectName.setBounds(20, 11, 80, 30);
		txtMilestoneName = new JTextField();
		txtMilestoneName.setBounds(101, 11, 200, 30);

		tblMilestone = new JTable();
		tblMilestone.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblMilestone.rowAtPoint(e.getPoint());
				int column = tblMilestone.columnAtPoint(e.getPoint());
				MilestoneModel pm = new MilestoneModel();

				// update row for project
//				if(column != 8) {
				Project_id = (String) tblMilestone.getValueAt(row, 0);
				pm.setMilestone_id(Integer.parseInt(Project_id));
				txtMilestoneName.setText((String) tblMilestone.getValueAt(row, 1));

				try {
					String dueDateStr = (String) tblMilestone.getValueAt(row, 2);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date start = sdf.parse(dueDateStr);
					txtDueDate.setDate(start);

				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String statusName = (String) tblMilestone.getValueAt(row, 3);
				cboStatus.setSelectedItem(statusName);

				String projectName = (String) tblMilestone.getValueAt(row, 4);
				cboProject.setSelectedItem(projectName);

				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				txtMilestoneName.requestFocus();
//				}

				// delete row for project
				if (column == 5) {
//		        	DefaultTableModel model = (DefaultTableModel) tblMilestone.getModel();
//	                String milestoneIdStr = (String) model.getValueAt(row, 0);
//	                int milestoneId = Integer.parseInt(milestoneIdStr);

					try {
						if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confrim",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							MilestoneController pc = new MilestoneController();
							int rs = pc.delete(pm);
							if (rs == 1) {

								JOptionPane.showMessageDialog(null, "Delete Successfully", "Successfully",
										JOptionPane.INFORMATION_MESSAGE);
//								AutoID();
								showList();
								clear();

							} else {
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
		JScrollPane tableScrollPane = new JScrollPane(tblMilestone);
		tableScrollPane.setBounds(23, 322, 661, 542);
		tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblDate = new JLabel("DueDate");
		lblDate.setBounds(20, 61, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblProjectName);
		formPanel.add(txtMilestoneName);
		formPanel.add(lblDate);

		txtDueDate = new JDateChooser();
		txtDueDate.setBounds(101, 61, 201, 30);
		txtDueDate.setDateFormatString("yyyy-MM-dd");
		formPanel.add(txtDueDate);

		JLabel lblNewLabel = new JLabel("Status");
		lblNewLabel.setBounds(332, 70, 46, 14);
		formPanel.add(lblNewLabel);

		cboStatus = new JComboBox();
		MySqlQuery.addCoboBox("status", "status_id", "status_name", cboStatus, statusMap);
		cboStatus.setBounds(413, 62, 200, 30);
		formPanel.add(cboStatus);

		JLabel lblClient = new JLabel("Project");
		lblClient.setBounds(331, 20, 46, 14);
		formPanel.add(lblClient);

		cboProject = new JComboBox();
		MySqlQuery.addCoboBox("project", "project_id", "project_name", cboProject, projectMap);
		cboProject.setBounds(412, 12, 200, 30);
		formPanel.add(cboProject);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MilestoneModel pm = new MilestoneModel();
				MilestoneController pc = new MilestoneController();
				// to write form validation
				if (txtMilestoneName.getText().trim().isEmpty() || txtDueDate.getDate() == null
						|| cboStatus.getSelectedIndex() == 0 || cboProject.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//				txtShowAll.requestFocus(true);
//				txtShowAll.selectAll();

				} else {

					pm.setName(txtMilestoneName.getText().toString());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String formattedDueDate = sdf.format(txtDueDate.getDate());
					pm.setDue_date(formattedDueDate);

					String selectedStatus = (String) cboStatus.getSelectedItem();
					int statusId = statusMap.get(selectedStatus);
					pm.setSts_id(statusId);

					String selectedProject = (String) cboProject.getSelectedItem();
					int projectId = projectMap.get(selectedProject);
					pm.setPj_id(projectId);

					if (Checking.IsValidName(pm.getName())) {
						JOptionPane.showMessageDialog(null, "Invlaid name field", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtMilestoneName.requestFocus(true);
						txtMilestoneName.selectAll();
					} else if (Checking.IsAllDigit(pm.getName())) {
						JOptionPane.showMessageDialog(null, "Company Name have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtMilestoneName.requestFocus(true);
						txtMilestoneName.selectAll();
					}
					else if (!Checking.validateFutureDate(pm.getDue_date())) {
						JOptionPane.showMessageDialog(null, "Invalid Start date", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtMilestoneName.requestFocus(true);
						txtMilestoneName.selectAll();
					}
//						else if (!Checking.validateEndDate(pm.getStart_date(), pm.getEnd_date())) {
//						JOptionPane.showMessageDialog(null, "Task Name have all digit", "Invlaid",
//								JOptionPane.ERROR_MESSAGE);
//						txtMilestoneName.requestFocus(true);
//						txtMilestoneName.selectAll();
//					}

					else {

						try {
							if (pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same project name!", "Fail",
										JOptionPane.ERROR_MESSAGE);
								txtMilestoneName.requestFocus(true);
								txtMilestoneName.selectAll();
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

		btnSave.setBounds(20, 126, 89, 30);
		formPanel.add(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MilestoneModel pm = new MilestoneModel();
				MilestoneController pc = new MilestoneController();

				if (txtMilestoneName.getText().trim().isEmpty() || txtDueDate.getDate() == null
						|| cboStatus.getSelectedIndex() == 0 || cboProject.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();

				} else {

					int r = tblMilestone.getSelectedRow();
					int milestone_id = Integer.parseInt(tblMilestone.getValueAt(r, 0).toString());
					pm.setMilestone_id(milestone_id);
					pm.setName(txtMilestoneName.getText().toString());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String formattedStartDate = sdf.format(txtDueDate.getDate());

					pm.setDue_date(formattedStartDate);

					String selectedStatus = (String) cboStatus.getSelectedItem();
					int statusId = statusMap.get(selectedStatus);
					pm.setSts_id(statusId);

					String selectedClient = (String) cboProject.getSelectedItem();
					int projectId = projectMap.get(selectedClient);
					pm.setPj_id(projectId);

					if (Checking.IsValidName(pm.getName())) {
						JOptionPane.showMessageDialog(null, "Invlaid name field", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtMilestoneName.requestFocus(true);
						txtMilestoneName.selectAll();
					} else if (Checking.IsAllDigit(pm.getName())) {
						JOptionPane.showMessageDialog(null, "Company Name have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtMilestoneName.requestFocus(true);
						txtMilestoneName.selectAll();
					}
					else if (!Checking.validateFutureDate(pm.getDue_date())) {
						JOptionPane.showMessageDialog(null, "Invalid Start date", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtMilestoneName.requestFocus(true);
						txtMilestoneName.selectAll();
					}

					else {

						try {
							if (pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same supplier name!", "Fail",
										JOptionPane.ERROR_MESSAGE);
								txtMilestoneName.requestFocus(true);
//						txtCustomerName.selectAll();
							} else {
								int rs = pc.update(pm);
								if (rs == 1) {
									JOptionPane.showMessageDialog(null, "Update Successfully", "Successfully",
											JOptionPane.INFORMATION_MESSAGE);
//							AutoID();
									clear();
									showList();
								}

							}
						} catch (HeadlessException | SQLException e1) {
//					// TODO Auto-generated catch block
							e1.printStackTrace();

						}
					}
				}
			}
		});
		btnUpdate.setBounds(143, 126, 89, 30);
		formPanel.add(btnUpdate);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				txtMilestoneName.requestFocus(true);
			}
		});
		btnClear.setBounds(260, 127, 89, 30);
		formPanel.add(btnClear);
		rightPanel.add(tableScrollPane);

		// Update component bounds on resize
		rightPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int padding = 16;
				int formHeight = 200;
				int formMarginBottom = 16;

				int width = rightPanel.getWidth();
				int height = rightPanel.getHeight();

				int innerWidth = width - (padding * 2);
				int innerHeight = height - (padding * 2);

				int tableY = padding + formHeight + formMarginBottom;
				int tableHeight = innerHeight - formHeight - formMarginBottom;

				formPanel.setBounds(padding, padding, innerWidth, formHeight);
				tableScrollPane.setBounds(padding, tableY, innerWidth, 460);
			}
		});

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);

		createTable();
		showList();

		btnUpdate.setEnabled(false);

		txtShowAll = new JTextField();
		txtShowAll.setBounds(890, 126, 200, 30);
		formPanel.add(txtShowAll);

		lblSearch = new JLabel("Search");
		lblSearch.setBounds(830, 134, 46, 14);
		formPanel.add(lblSearch);

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
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) tblMilestone.getColumnModel();
		TableColumn tc = tcm.getColumn(index);
		tc.setPreferredWidth(width);
	}

	public void createTable() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("DueDate");
		dtm.addColumn("Status");
		dtm.addColumn("Project");
		dtm.addColumn("");
		tblMilestone.setModel(dtm);
		tblMilestone.setRowHeight(25);
		setColumnWidth(0, 60);
		setColumnWidth(1, 60);
		setColumnWidth(2, 150);
		setColumnWidth(3, 100);
		setColumnWidth(4, 100);
		setColumnWidth(5, 100);

		// Customize table header
		JTableHeader header = tblMilestone.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Set header height

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment

		// Apply center alignment to specific columns
		tblMilestone.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tblMilestone.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		tblMilestone.getColumnModel().getColumn(5).setCellRenderer(createButtonCellRenderer("Delete"));

		// Set custom header renderer
		DefaultTableCellRenderer headerRenderer = createHeaderRenderer();

		// Apply header renderer to all columns
		for (int i = 0; i < tblMilestone.getColumnModel().getColumnCount(); i++) {
			tblMilestone.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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
		txtMilestoneName.setText("");
		txtDueDate.setDate(null);
		cboStatus.setSelectedIndex(0);
		cboProject.setSelectedIndex(0);

		txtMilestoneName.requestFocus(true);
	}

	public void showListOne() throws SQLException {
		String data[] = new String[6];
		MilestoneController cc = new MilestoneController();
		MilestoneModel cm = new MilestoneModel();
		cm.setName(txtShowAll.getText().toString().trim());
		List<MilestoneModel> list = cc.selectone(cm);
		dtm.setRowCount(0);
		for (MilestoneModel c : list) {
			data[0] = Integer.toString(c.getMilestone_id());
			;
			data[1] = c.getName();
			data[2] = c.getDue_date();
			data[3] = getNameById(statusMap, c.getSts_id());
			data[4] = getNameById(projectMap, c.getPj_id());
			data[5] = "Delete";
			dtm.addRow(data);
		}
	}

	public void showList() {
		String data[] = new String[9];
		MilestoneController pc = new MilestoneController();
		try {
			List<MilestoneModel> list = pc.selectall();
			dtm.setRowCount(0);
			for (MilestoneModel pm : list) {
				data[0] = Integer.toString(pm.getMilestone_id());
				;
				data[1] = pm.getName();
				data[2] = pm.getDue_date();
				data[3] = getNameById(statusMap, pm.getSts_id());
				data[4] = getNameById(projectMap, pm.getPj_id());
				data[5] = "Delete";
				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get id from database and show name by Id
	public static String getNameById(Map<String, Integer> map, int id) {
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() == id) {
				return entry.getKey();
			}
		}
		return null;
	}
}
