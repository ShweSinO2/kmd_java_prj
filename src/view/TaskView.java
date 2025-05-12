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
import controller.TaskController;
import model.MilestoneModel;
import model.TaskModel;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

public class TaskView extends JFrame {

	DefaultTableModel dtm = new DefaultTableModel();
	private JTable tblTask;
	private JButton btnSave;
	private JButton btnUpdate;
	private JButton btnClear;
	private JTextField txtTaskName;
	private JDateChooser txtStartDate;
	private JDateChooser txtEndDate;
	private JComboBox cboStatus;
	private JComboBox cboProject;
	private JComboBox cboMilestone;
	private JComboBox cboAssigned;
	private JComboBox cboPriority;
	private JComboBox cboType;
	String Task_id = null;
	Map<String, Integer> ProjectMap = new HashMap<>();
	Map<String, Integer> MilestoneMap = new HashMap<>();
	Map<String, String> AssignedMap = new HashMap<>();
	Map<String, Integer> StatusMap = new HashMap<>();
	Map<String, Integer> PriorityMap = new HashMap<>();
	Map<String, Integer> TypeMap = new HashMap<>();

	private JTextField txtDescription;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			TaskView taskFrame = new TaskView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			taskFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			taskFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			taskFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TaskView() {
		setTitle("Task");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel("TaskView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBounds(10, 0, 661, 324);
		formPanel.setBackground(new Color(255, 255, 255));

		JLabel lblTaskName = new JLabel("Name:");
		lblTaskName.setBounds(20, 11, 80, 30);
		txtTaskName = new JTextField();
		txtTaskName.setBounds(101, 11, 200, 30);

		tblTask = new JTable();
		tblTask.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = tblTask.rowAtPoint(e.getPoint());
				int column = tblTask.columnAtPoint(e.getPoint());
				TaskModel pm = new TaskModel();

				// update row for task
//				if(column != 8) {
				Task_id = (String) tblTask.getValueAt(row, 0);
				pm.setMilestone_id(Integer.parseInt(Task_id));
				txtTaskName.setText((String) tblTask.getValueAt(row, 1));
				txtDescription.setText((String) tblTask.getValueAt(row, 2));

				try {
					String StartDateStr = (String) tblTask.getValueAt(row, 3);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date start = sdf.parse(StartDateStr);
					txtStartDate.setDate(start);

					String EndDateStr = (String) tblTask.getValueAt(row, 4);

//				        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date end = sdf.parse(EndDateStr);
					txtEndDate.setDate(end);

				} catch (ParseException e1) {
					e1.printStackTrace();
				}

				String projectName = (String) tblTask.getValueAt(row, 5);
				cboProject.setSelectedItem(projectName);

				String milestoneName = (String) tblTask.getValueAt(row, 6);
				cboMilestone.setSelectedItem(milestoneName);

				String assignedName = (String) tblTask.getValueAt(row, 7);
				cboAssigned.setSelectedItem(assignedName);

				String statusName = (String) tblTask.getValueAt(row, 8);
				cboStatus.setSelectedItem(statusName);

				String priorityName = (String) tblTask.getValueAt(row, 9);
				cboPriority.setSelectedItem(priorityName);

				String typeName = (String) tblTask.getValueAt(row, 10);
				cboType.setSelectedItem(typeName);

				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				txtTaskName.requestFocus();
//				}

				// delete row for project
				if (column == 11) {
//		        	DefaultTableModel model = (DefaultTableModel) tblTask.getModel();
//	                String milestoneIdStr = (String) model.getValueAt(row, 0);
//	                int TaskId = Integer.parseInt(milestoneIdStr);

					try {
						if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confrim",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							TaskController pc = new TaskController();
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
		JScrollPane tableScrollPane = new JScrollPane(tblTask);
		tableScrollPane.setBounds(23, 322, 661, 542);
		tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblDate = new JLabel("StartDate");
		lblDate.setBounds(20, 59, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblTaskName);
		formPanel.add(txtTaskName);
		formPanel.add(lblDate);

		txtStartDate = new JDateChooser();
		txtStartDate.setBounds(101, 63, 201, 30);
		txtStartDate.setDateFormatString("yyyy-MM-dd");
		formPanel.add(txtStartDate);

		JLabel lblNewLabel = new JLabel("Status");
		lblNewLabel.setBounds(321, 114, 46, 14);
		formPanel.add(lblNewLabel);

		JLabel lblMilestone = new JLabel("Milestone");
		lblMilestone.setBounds(20, 161, 80, 14);
		formPanel.add(lblMilestone);

		cboProject = new JComboBox();
		MySqlQuery.addCoboBox("project", "project_id", "project_name", cboProject, ProjectMap);
		cboProject.setBounds(101, 110, 200, 30);
		formPanel.add(cboProject);
		rightPanel.add(tableScrollPane);

		cboMilestone = new JComboBox();
		cboMilestone.setBounds(101, 157, 200, 30);
		formPanel.add(cboMilestone);

		cboAssigned = new JComboBox();
		cboAssigned.setBounds(101, 211, 200, 30);
		formPanel.add(cboAssigned);

//		cboProject.addActionListener(new ActionListener () {
//			public void actionPerformed(ActionEvent e) {
//				String selectedStatus = (String) cboProject.getSelectedItem();
//				int ProjectId = ProjectMap.get(selectedStatus);
//				MySqlQuery.addCoboBoxV1( cboMilestone, MilestoneMap,ProjectId);
//				
//				cboMilestone.addActionListener(new ActionListener () {
//					public void actionPerformed(ActionEvent e) {
//						String selectedStatus = (String) cboMilestone.getSelectedItem();
//						int MilestoneId = MilestoneMap.get(selectedStatus);
//						MySqlQuery.addCoboBoxV2( cboAssigned, AssignedMap,MilestoneId);
//						
//						
//						
//					}
//				}); 
//				
//			}
//		}); 

		// First: Add milestone combo box listener only once
		cboMilestone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedStatus = (String) cboMilestone.getSelectedItem();
				if (selectedStatus != null && MilestoneMap.containsKey(selectedStatus)) {
					int MilestoneId = MilestoneMap.get(selectedStatus);
					MySqlQuery.addCoboBoxV2(cboAssigned, AssignedMap, MilestoneId);
				}
			}
		});

		// Then: Add project combo box listener
		cboProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedStatus = (String) cboProject.getSelectedItem();
				if (selectedStatus != null && ProjectMap.containsKey(selectedStatus)) {
					int ProjectId = ProjectMap.get(selectedStatus);
					MySqlQuery.addCoboBoxV1(cboMilestone, MilestoneMap, ProjectId);
				}
			}
		});

		cboStatus = new JComboBox();
		MySqlQuery.addCoboBox("status", "status_id", "status_name", cboStatus, StatusMap);
		cboStatus.setBounds(389, 106, 200, 30);
		formPanel.add(cboStatus);

		cboPriority = new JComboBox();
		MySqlQuery.addCoboBox("task_priority", "priority_id", "priority_name", cboPriority, PriorityMap);
		cboPriority.setBounds(389, 161, 200, 30);
		formPanel.add(cboPriority);

		cboType = new JComboBox();
		MySqlQuery.addCoboBox("task_type", "type_id", "type_name", cboType, TypeMap);
		cboType.setBounds(389, 211, 200, 30);
		formPanel.add(cboType);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TaskModel pm = new TaskModel();
				TaskController pc = new TaskController();
				// to write form validation
				if (txtTaskName.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty() ||

						txtStartDate.getDate() == null || txtEndDate.getDate() == null
						|| cboProject.getSelectedIndex() == 0 || cboMilestone.getSelectedIndex() == 0
						|| cboAssigned.getSelectedIndex() == 0 || cboStatus.getSelectedIndex() == 0
						|| cboPriority.getSelectedIndex() == 0 || cboType.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//				txtShowAll.requestFocus(true);
//				txtShowAll.selectAll();

				} else {

					pm.setTask_name(txtTaskName.getText().toString());
					pm.setDescription(txtDescription.getText().toString());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String formattedStartDate = sdf.format(txtStartDate.getDate());
					pm.setStart_date(formattedStartDate);

					String formattedEndDate = sdf.format(txtEndDate.getDate());
					pm.setEnd_date(formattedEndDate);

					String selectedProject = (String) cboProject.getSelectedItem();
					int projectId = ProjectMap.get(selectedProject);
					pm.setProject_id(projectId);

					String selectedMilestone = (String) cboMilestone.getSelectedItem();
					int milestoneId = MilestoneMap.get(selectedMilestone);
					pm.setMilestone_id(milestoneId);

					String selectedAssigned = (String) cboAssigned.getSelectedItem();
					String assignedId = AssignedMap.get(selectedAssigned);
					pm.setAssigned_id(assignedId);

					String selectedStatus = (String) cboStatus.getSelectedItem();
					int statusId = StatusMap.get(selectedStatus);
					pm.setStatus_id(statusId);

					String selectedPriority = (String) cboPriority.getSelectedItem();
					int priorityId = PriorityMap.get(selectedPriority);
					pm.setPriority_id(priorityId);

					String selectedType = (String) cboType.getSelectedItem();
					int typeId = TypeMap.get(selectedType);
					pm.setType_id(typeId);

					if (Checking.IsValidName(pm.getTask_name())) {
						JOptionPane.showMessageDialog(null, "Invlaid name field", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtTaskName.requestFocus(true);
						txtTaskName.selectAll();
					} else if (Checking.IsAllDigit(pm.getTask_name())) {
						JOptionPane.showMessageDialog(null, "Task Name have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtTaskName.requestFocus(true);
						txtTaskName.selectAll();
					}

					else {

						try {
							if (pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same project name!", "Fail",
										JOptionPane.ERROR_MESSAGE);
								txtTaskName.requestFocus(true);
								txtTaskName.selectAll();
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

		btnSave.setBounds(20, 269, 89, 30);
		formPanel.add(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TaskModel pm = new TaskModel();
				TaskController pc = new TaskController();

				if (txtTaskName.getText().trim().isEmpty() || txtDescription.getText().trim().isEmpty() ||

						txtStartDate.getDate() == null || txtEndDate.getDate() == null
						|| cboProject.getSelectedIndex() == 0 || cboMilestone.getSelectedIndex() == 0
						|| cboAssigned.getSelectedIndex() == 0 || cboStatus.getSelectedIndex() == 0
						|| cboPriority.getSelectedIndex() == 0 || cboType.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//				txtShowAll.requestFocus(true);
//				txtShowAll.selectAll();

				} else {

					int r = tblTask.getSelectedRow();
					int Task_id = Integer.parseInt(tblTask.getValueAt(r, 0).toString());
					pm.setTask_id(Task_id);

					pm.setTask_name(txtTaskName.getText().toString());
					pm.setDescription(txtDescription.getText().toString());

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String formattedStartDate = sdf.format(txtStartDate.getDate());
					pm.setStart_date(formattedStartDate);

					String formattedEndDate = sdf.format(txtEndDate.getDate());
					pm.setEnd_date(formattedEndDate);

					String selectedProject = (String) cboProject.getSelectedItem();
					int projectId = ProjectMap.get(selectedProject);
					pm.setProject_id(projectId);

					String selectedMilestone = (String) cboMilestone.getSelectedItem();
					int milestoneId = MilestoneMap.get(selectedMilestone);
					pm.setMilestone_id(milestoneId);

					String selectedAssigned = (String) cboAssigned.getSelectedItem();
					String assignedId = AssignedMap.get(selectedAssigned);
					pm.setAssigned_id(assignedId);

					String selectedStatus = (String) cboStatus.getSelectedItem();
					int statusId = StatusMap.get(selectedStatus);
					pm.setStatus_id(statusId);
//					pc.updateTask(statusId,Task_id);

					String selectedPriority = (String) cboPriority.getSelectedItem();
					int priorityId = PriorityMap.get(selectedPriority);
					pm.setPriority_id(priorityId);

					String selectedType = (String) cboType.getSelectedItem();
					int typeId = TypeMap.get(selectedType);
					pm.setType_id(typeId);

					if (Checking.IsValidName(pm.getTask_name())) {
						JOptionPane.showMessageDialog(null, "Invlaid name field", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtTaskName.requestFocus(true);
						txtTaskName.selectAll();
					} else if (Checking.IsAllDigit(pm.getTask_name())) {
						JOptionPane.showMessageDialog(null, "Task Name have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtTaskName.requestFocus(true);
						txtTaskName.selectAll();
					}

					else {

						try {							
								int rs = pc.update(pm);
								System.out.println(rs);
								if (rs == 1) {
									JOptionPane.showMessageDialog(null, "Update Successfully", "Successfully",
											JOptionPane.INFORMATION_MESSAGE);
									clear();
									showList();
							}

							
						} catch (HeadlessException e1) {
//					// TODO Auto-generated catch block
							e1.printStackTrace();

						}
					}
				}
			}
		});
		btnUpdate.setBounds(137, 269, 89, 30);
		formPanel.add(btnUpdate);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				txtTaskName.requestFocus(true);
			}
		});
		btnClear.setBounds(252, 269, 89, 30);
		formPanel.add(btnClear);

		JLabel lblTaskName_1 = new JLabel("Description");
		lblTaskName_1.setBounds(321, 11, 80, 30);
		formPanel.add(lblTaskName_1);

		JLabel lblTaskName_2 = new JLabel("EndDate");
		lblTaskName_2.setBounds(321, 59, 80, 30);
		formPanel.add(lblTaskName_2);

		JLabel lblTaskName_3 = new JLabel("Priority");
		lblTaskName_3.setBounds(321, 161, 80, 30);
		formPanel.add(lblTaskName_3);

		JLabel lblTaskName_4 = new JLabel("Type");
		lblTaskName_4.setBounds(321, 211, 80, 30);
		formPanel.add(lblTaskName_4);

		txtDescription = new JTextField();
		txtDescription.setBounds(389, 11, 200, 30);
		formPanel.add(txtDescription);

		txtEndDate = new JDateChooser();
		txtEndDate.setDateFormatString("yyyy-MM-dd");
		txtEndDate.setBounds(389, 59, 201, 30);
		formPanel.add(txtEndDate);

		JLabel lblAssignedTo = new JLabel("Assigned to");
		lblAssignedTo.setBounds(20, 219, 80, 14);
		formPanel.add(lblAssignedTo);

		JLabel lblProject = new JLabel("Project");
		lblProject.setBounds(20, 114, 80, 14);
		formPanel.add(lblProject);

		rightPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int padding = 16;
				int formHeight = 330;
				int formMarginBottom = 16;

				int width = rightPanel.getWidth();
				int height = rightPanel.getHeight();

				int innerWidth = width - (padding * 2);
				int innerHeight = height - (padding * 2);

				int tableY = padding + formHeight + formMarginBottom;
				int tableHeight = innerHeight - formHeight - formMarginBottom;

				formPanel.setBounds(padding, padding, innerWidth, formHeight);
				tableScrollPane.setBounds(padding, tableY, innerWidth, 330);
			}
		});

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);

		createTable();
		showList();

		btnUpdate.setEnabled(false);

	}

	public void setColumnWidth(int index, int width) {
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) tblTask.getColumnModel();
		TableColumn tc = tcm.getColumn(index);
		tc.setPreferredWidth(width);
	}

	public void createTable() {
		dtm.addColumn("ID");
		dtm.addColumn("Name");
		dtm.addColumn("Description");
		dtm.addColumn("StartDate");
		dtm.addColumn("EndDate");
		dtm.addColumn("Project");
		dtm.addColumn("Milestone");
		dtm.addColumn("Assigned to");
		dtm.addColumn("Status");
		dtm.addColumn("Priority");
		dtm.addColumn("Type");
		dtm.addColumn("");
		tblTask.setModel(dtm);
		tblTask.setRowHeight(25);
		setColumnWidth(0, 60);
		setColumnWidth(1, 150);
		setColumnWidth(2, 150);
		setColumnWidth(3, 100);
		setColumnWidth(4, 100);
		setColumnWidth(5, 100);
		setColumnWidth(6, 150);
		setColumnWidth(7, 120);
		setColumnWidth(8, 100);
		setColumnWidth(9, 100);
		setColumnWidth(10, 100);
		setColumnWidth(11, 80);

		// Customize table header
		JTableHeader header = tblTask.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Set header height

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment

		// Apply center alignment to specific columns
		tblTask.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tblTask.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		tblTask.getColumnModel().getColumn(11).setCellRenderer(createButtonCellRenderer("Delete"));

		// Set custom header renderer
		DefaultTableCellRenderer headerRenderer = createHeaderRenderer();

		// Apply header renderer to all columns
		for (int i = 0; i < tblTask.getColumnModel().getColumnCount(); i++) {
			tblTask.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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
				label.setForeground(new Color(220, 53, 69));
				label.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
		txtDescription.setText("");
		txtTaskName.setText("");
		txtStartDate.setDate(null);
		txtEndDate.setDate(null);
		cboProject.setSelectedIndex(0);
		cboStatus.setSelectedIndex(0);
		cboMilestone.setSelectedIndex(0);
		cboAssigned.setSelectedIndex(0);
		cboPriority.setSelectedIndex(0);
		cboType.setSelectedIndex(0);

		txtTaskName.requestFocus(true);
	}

	public void showList() {
		String data[] = new String[12];
		TaskController pc = new TaskController();
		try {
			List<TaskModel> list = pc.selectall();
			dtm.setRowCount(0);
			for (TaskModel pm : list) {
				MySqlQuery.getComboData2("employee", "employee_id", "name", AssignedMap);
				MySqlQuery.getComboData("milestone", "milestone_id", "milestone_name", MilestoneMap);

				data[0] = Integer.toString(pm.getTask_id());
				;
				data[1] = pm.getTask_name();
				data[2] = pm.getDescription();
				data[3] = pm.getStart_date();
				data[4] = pm.getEnd_date();
				data[5] = pc.returnPjName(pm.getMilestone_id());
				data[6] = getNameById(MilestoneMap, pm.getMilestone_id());
				data[7] = getNameById1(AssignedMap, pm.getAssigned_id());
				data[8] = getNameById(StatusMap, pm.getStatus_id());
				data[9] = getNameById(PriorityMap, pm.getPriority_id());
				data[10] = getNameById(TypeMap, pm.getType_id());
				data[11] = "Delete";
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

	public static String getNameById1(Map<String, String> map, String id) {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (entry.getValue().equals(id)) {
				return entry.getKey();
			}
		}
		return null;
	}
}
