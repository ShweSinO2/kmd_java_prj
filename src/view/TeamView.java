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
import controller.EmployeeController;
import controller.TeamController;
import model.TeamModel;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
	private JButton btnClear;
	private JTextField txtTeamName;
	String username,password;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		try {
//			TeamView cltFrame = new TeamView();
//			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//			cltFrame.setBounds(0, 0, screenSize.width, screenSize.height);
//			cltFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			cltFrame.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

	public TeamView(String username,String password) {
		this.username = username;
		this.password = password;
		
		setTitle("Team");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);

		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel(username,password,"TeamView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBounds(10, 0, 661, 293);
		formPanel.setBackground(new Color(255, 255, 255));

		tblTeam = new JTable();
		tblTeam.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TeamController ec = new TeamController();
				int r = tblTeam.getSelectedRow();
				int column = tblTeam.columnAtPoint(e.getPoint());
				TeamModel pm = new TeamModel();
				String Team_id = (String) tblTeam.getValueAt(r, 0);

				txtTeamName.setText((String) tblTeam.getValueAt(r, 1));

				btnSave.setEnabled(false);
				btnUpdate.setEnabled(true);
				txtTeamName.requestFocus();
				txtTeamName.selectAll();
				if (column == 2) {
					DefaultTableModel model = (DefaultTableModel) tblTeam.getModel();
					String teamId = (String) model.getValueAt(r, 0);
					pm.setTeam_id(Integer.parseInt(teamId));

					try {
						if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete?", "Confrim",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							TeamController pc = new TeamController();
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
		JScrollPane tableScrollPane = new JScrollPane(tblTeam);
		tableScrollPane.setBounds(23, 322, 661, 542);
		tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		JLabel lblTeamName = new JLabel("Team Name");
		lblTeamName.setBounds(20, 21, 80, 30);
		formPanel.setLayout(null);
		formPanel.add(lblTeamName);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamModel pm = new TeamModel();
				TeamController pc = new TeamController();

				if (txtTeamName.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else {

					pm.setTeam_name(txtTeamName.getText().toString());

					if (Checking.IsValidName(pm.getTeam_name())) {
						JOptionPane.showMessageDialog(null, "Invlaid related field", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					} else if (Checking.IsAllDigit(pm.getTeam_name())) {
						JOptionPane.showMessageDialog(null, "All digit Error", "Invlaid", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
					} else {

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
		btnSave.setBounds(20, 78, 89, 30);
		formPanel.add(btnSave);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TeamModel pm = new TeamModel();
				TeamController pc = new TeamController();
				if (txtTeamName.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "There is a blank field!", "Fail", JOptionPane.ERROR_MESSAGE);
//					txtShowAll.requestFocus(true);
//					txtShowAll.selectAll();
				} else {

					int r = tblTeam.getSelectedRow();
					int Client_id = Integer.parseInt(tblTeam.getValueAt(r, 0).toString());
					pm.setTeam_id(Client_id);
					pm.setTeam_name(txtTeamName.getText().toString());

//				int statusId = Integer.parseInt(cboRoleID.getSelectedItem().toString());
//				pm.setRole_id(statusId);

					if (Checking.IsValidName(pm.getTeam_name())) {
						JOptionPane.showMessageDialog(null, "Invlaid name field", "Invlaid", JOptionPane.ERROR_MESSAGE);
						txtTeamName.requestFocus(true);
						txtTeamName.selectAll();
					} else if (Checking.IsAllDigit(pm.getTeam_name())) {
						JOptionPane.showMessageDialog(null, "Team Name have all digit", "Invlaid",
								JOptionPane.ERROR_MESSAGE);
						txtTeamName.requestFocus(true);
						txtTeamName.selectAll();
					}

					else {
						try {
							if (pc.isduplicate(pm)) {
								JOptionPane.showMessageDialog(null, "There is a same Team name!", "Fail",
										JOptionPane.ERROR_MESSAGE);
								txtTeamName.requestFocus(true);
								txtTeamName.selectAll();
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
		btnUpdate.setBounds(135, 78, 89, 30);
		formPanel.add(btnUpdate);

		btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				txtTeamName.requestFocus(true);
			}
		});
		btnClear.setBounds(254, 78, 89, 30);
		formPanel.add(btnClear);

		txtTeamName = new JTextField();
		txtTeamName.setBounds(110, 21, 200, 30);
		formPanel.add(txtTeamName);
		txtTeamName.setColumns(10);

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

		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);

		createTable();
		showList();
		
		btnUpdate.setEnabled(false);

	}

	public void setColumnWidth(int index, int width) {
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) tblTeam.getColumnModel();
		TableColumn tc = tcm.getColumn(index);
		tc.setPreferredWidth(width);
	}

	public void createTable() {
		dtm.addColumn("Team ID");
		dtm.addColumn("Team Name");
		dtm.addColumn("");
		tblTeam.setModel(dtm);
		tblTeam.setRowHeight(25);
		setColumnWidth(0, 60);
		setColumnWidth(1, 60);
		setColumnWidth(1, 60);

		// Customize table header
		JTableHeader header = tblTeam.getTableHeader();
		header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Set header height

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment

		// Apply center alignment to specific columns
		tblTeam.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tblTeam.getColumnModel().getColumn(2).setCellRenderer(createButtonCellRenderer("Delete"));

		// Set custom header renderer
		DefaultTableCellRenderer headerRenderer = createHeaderRenderer();

		// Apply header renderer to all columns
		for (int i = 0; i < tblTeam.getColumnModel().getColumnCount(); i++) {
			tblTeam.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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
		txtTeamName.setText("");
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
				data[2] = "Delete";

				dtm.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
