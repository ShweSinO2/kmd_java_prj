package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import config.DBConfig;
import config.MySqlQuery;
import controller.AttachmentController;
import model.AttachmentModel;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class AttachmentView extends JFrame {
	DefaultTableModel dtm = new DefaultTableModel();
	private JButton uploadBtn;
	private JTable tblAttachment;
	private String Attachment_id = null;
	private JLabel lblSelectedFile;
	private File selectedFile = null;
	private String originalName = "";
	private String timestamp = "";
	private String ext = "";
	JComboBox cboRelatedName;
	JComboBox cboRelatedType;
	Map<String, Integer> projectMap = new HashMap<>();
	Map<String, Integer> taskMap = new HashMap<>();
	Map<String, Integer> dataMap = new HashMap<>();
	
	public static Connection con = null;
	static {
		DBConfig cls = new DBConfig();
		try {
			con = cls.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Insert Fail,Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}
	}

	public AttachmentView() {
		setTitle("File Upload/Download");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900, 600);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());

		SideMenuPanel sideMenu = new SideMenuPanel("AttachmentView");
		getContentPane().add(sideMenu, BorderLayout.WEST);

		JPanel rightPanel = new JPanel();

		JPanel formPanel = new JPanel();
		formPanel.setBackground(new Color(255, 255, 255));
		formPanel.setBounds(10, 0, 661, 293);
		formPanel.setLayout(null);

		uploadBtn = new JButton("Upload File");
		uploadBtn.setBounds(72, 20, 120, 30);
		formPanel.add(uploadBtn);
		
		// Label to show selected file name
		lblSelectedFile = new JLabel("No file selected");
		lblSelectedFile.setBounds(72, 55, 400, 20); // adjust as needed
		formPanel.add(lblSelectedFile);

		tblAttachment = new JTable();
		tblAttachment.setRowHeight(20);
		tblAttachment.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AttachmentModel am = new AttachmentModel();
				
				int row = tblAttachment.rowAtPoint(e.getPoint());
				int column = tblAttachment.columnAtPoint(e.getPoint());

				Attachment_id = (String) tblAttachment.getValueAt(row, 0);
				am.setAttachment_id(Integer.parseInt(Attachment_id));
				am.setFilename((String) tblAttachment.getValueAt(row, 1));
				
				//download for attachment
				if(column == 4) {
					DefaultTableModel model = (DefaultTableModel) tblAttachment.getModel();
	                String attachmentIdStr = (String) model.getValueAt(row, 0);
	                int attachmentId = Integer.parseInt(attachmentIdStr);
	                
	                downloadFile(attachmentId);
				}
			}
		});
		
		JScrollPane tableScrollPane = new JScrollPane(tblAttachment);
		tableScrollPane.setBounds(10, 318, 664, 271);
		tableScrollPane.getViewport().setBackground(new Color(255, 255, 255));
		rightPanel.setLayout(null);

		// Add to right panel
		rightPanel.add(formPanel);

		cboRelatedType = new JComboBox();
		cboRelatedType.setModel(new DefaultComboBoxModel(new String[] { "-Select-", "Project", "Task" }));
		cboRelatedType.setBounds(300, 20, 120, 31);
		formPanel.add(cboRelatedType);

		JLabel lblRelatedTypeLabel = new JLabel(" RelatedType");
		lblRelatedTypeLabel.setBounds(205, 28, 82, 14);
		formPanel.add(lblRelatedTypeLabel);

		JLabel lblUploadFile = new JLabel("Upload");
		lblUploadFile.setBounds(13, 28, 46, 14);
		formPanel.add(lblUploadFile);

		JLabel lblRelatedNameLabel = new JLabel(" RelatedName");
		lblRelatedNameLabel.setBounds(433, 28, 82, 14);
		formPanel.add(lblRelatedNameLabel);

		cboRelatedName = new JComboBox();
		cboRelatedName.setBounds(528, 20, 120, 31);
		formPanel.add(cboRelatedName);
		
		// Listener to handle type selection
		cboRelatedType.addActionListener(new ActionListener() {
		    @Override
			public void actionPerformed(ActionEvent e) {
	        String selectedType = (String) cboRelatedType.getSelectedItem();
	        cboRelatedName.removeAllItems(); // clear current items

	        if ("Project".equals(selectedType)) {
	            MySqlQuery.addCoboBox("project", "project_id", "project_name", cboRelatedName, dataMap);
	        } else if ("Task".equals(selectedType)) {
	            MySqlQuery.addCoboBox("task", "task_id", "task_name", cboRelatedName, dataMap);
	        }
		    }
		});

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(e -> saveFile());
		btnSave.setBounds(22, 90, 89, 23);
		formPanel.add(btnSave);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(148, 90, 89, 23);
		formPanel.add(btnUpdate);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(274, 90, 89, 23);
		formPanel.add(btnClear);

		rightPanel.add(tableScrollPane);
		
		// Update component bounds on resize
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
		
		// Add right panel to main frame
		getContentPane().add(rightPanel, BorderLayout.CENTER);

		createTable();
		showList();

		uploadBtn.addActionListener(e -> uploadFile());
	}

	public void setColumnWidth(int index, int width) {
		DefaultTableColumnModel tcm = (DefaultTableColumnModel) tblAttachment.getColumnModel();
		TableColumn tc = tcm.getColumn(index);
		tc.setPreferredWidth(width);
	}

	public void createTable() {
		dtm.addColumn("ID");
		dtm.addColumn("FileName");
		dtm.addColumn("RelatedEntityName");
		dtm.addColumn("RelatedEntityType");
		dtm.addColumn("");
		dtm.addColumn("");
		tblAttachment.setModel(dtm);
		tblAttachment.setRowHeight(25);
		setColumnWidth(0, 20);
		setColumnWidth(1, 200);
		setColumnWidth(2, 150);
		setColumnWidth(3, 150);
		setColumnWidth(4, 150);
		setColumnWidth(5, 150);

	    // Customize table header
	    JTableHeader header = tblAttachment.getTableHeader();
	    header.setPreferredSize(new Dimension(header.getWidth(), 30)); // Set header height
	    
	    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment
	    
	 // Apply center alignment to specific columns
	    tblAttachment.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	    tblAttachment.getColumnModel().getColumn(4).setCellRenderer(createButtonCellRenderer("Download"));
	    tblAttachment.getColumnModel().getColumn(5).setCellRenderer(createButtonCellRenderer("Delete"));
	    
	    // Set custom header renderer
	    DefaultTableCellRenderer headerRenderer = createHeaderRenderer();
	    
	    // Apply header renderer to all columns
	    for (int i = 0; i < tblAttachment.getColumnModel().getColumnCount(); i++) {
	        tblAttachment.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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
          	  	label.setForeground(new Color(40, 167, 69));
          	  	label.setCursor(new Cursor(Cursor.HAND_CURSOR));
	            
	            //download design
	            if(buttonText == "Delete") {
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
        
		AttachmentController ac = new AttachmentController();
		try {
			List<AttachmentModel> list = ac.selectall();
			dtm.setRowCount(0);
			for (AttachmentModel am : list) {
				
				MySqlQuery.getComboData("project", "project_id", "project_name", projectMap);
				MySqlQuery.getComboData("task", "task_id", "task_name", taskMap);
		        
				data[0] = Integer.toString(am.getAttachment_id());
				data[1] = am.getFilename();
				
				if ("Project".equals(am.getRelated_type())) {
					data[2] = getNameById(projectMap, am.getRelated_id());
			    } else if ("Task".equals(am.getRelated_type())) {
			        data[2] = getNameById(taskMap, am.getRelated_id());
			    }
				
				data[3] = am.getRelated_type();
				data[4] = "Download";
				data[5] = "Delete";
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
	
	private void uploadFile() {
	    JFileChooser chooser = new JFileChooser();
	    int option = chooser.showOpenDialog(this);

	    if (option == JFileChooser.APPROVE_OPTION) {
	        selectedFile = chooser.getSelectedFile();
	        originalName = selectedFile.getName();
	        lblSelectedFile.setText(originalName); // Show filename

	        // Get file extension
	        int i = originalName.lastIndexOf('.');
	        ext = (i > 0) ? originalName.substring(i) : "";
	        timestamp = String.valueOf(System.currentTimeMillis());
	    }
	}

	private void saveFile() {
	    if (selectedFile == null) {
	        JOptionPane.showMessageDialog(this, "Please select a file before saving.");
	        return;
	    }

	    String selectedType = (String) cboRelatedType.getSelectedItem();
	    String selectedName = (String) cboRelatedName.getSelectedItem();

	    if (selectedType == null || selectedName == null) {
	        JOptionPane.showMessageDialog(this, "Please select related type and name.");
	        return;
	    }
	    
	    int relatedId = dataMap.get(selectedName);

	    File destDir = new File("upload_dir");
	    if (!destDir.exists()) destDir.mkdir();

	    try {
	        // Insert into DB
	        PreparedStatement ps = con.prepareStatement(
	            "INSERT INTO pj_management.attachment (filename, temp_filename, filepath, employee_id, uploaded_date, related_entity_type, related_entity_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
	            Statement.RETURN_GENERATED_KEYS
	        );
	        ps.setString(1, originalName);
	        ps.setString(2, "");
	        ps.setString(3, "");
	        ps.setString(4, "E-000001"); // Replace with dynamic user
	        ps.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	        ps.setString(6, selectedType);
	        ps.setInt(7, relatedId);
	        ps.executeUpdate();

	        ResultSet rs = ps.getGeneratedKeys();
	        int attachmentId = rs.next() ? rs.getInt(1) : -1;

	        // Copy file
	        String newFilename = "attachment_" + attachmentId + "_" + timestamp + ext;
	        File destFile = new File(destDir, newFilename);
	        Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

	        // Update DB with actual filename and path
	        String updateSQL = "UPDATE pj_management.attachment SET temp_filename = ?, filepath = ? WHERE attachment_id = ?";
	        PreparedStatement psUpdate = con.prepareStatement(updateSQL);
	        psUpdate.setString(1, newFilename);
	        psUpdate.setString(2, destFile.getAbsolutePath());
	        psUpdate.setInt(3, attachmentId);
	        psUpdate.executeUpdate();

	        JOptionPane.showMessageDialog(this, "File saved successfully.");
	        showList();

	        // Reset
	        selectedFile = null;
	        lblSelectedFile.setText("No file selected");

	    } catch (IOException | SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error saving file.");
	    }
	}

	private void downloadFile(int attachment_id) {
		AttachmentModel am = new AttachmentModel();

		try {
			PreparedStatement ps = (PreparedStatement) con.prepareStatement(
					"SELECT filename,temp_filename, filepath FROM pj_management.attachment WHERE attachment_id = ?");
			ps.setInt(1, attachment_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				File sourceFile = new File(rs.getString("filepath"));
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(new File(rs.getString("filename")));
				int option = chooser.showSaveDialog(this);
				if (option == JFileChooser.APPROVE_OPTION) {
					Files.copy(sourceFile.toPath(), chooser.getSelectedFile().toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					JOptionPane.showMessageDialog(this, "File downloaded successfully.");
				}
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			AttachmentView attachFrame = new AttachmentView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			attachFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			attachFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			attachFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
