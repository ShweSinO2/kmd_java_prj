package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import controller.EmployeeController;
import model.EmployeeModel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.*;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
        setLayout(null); // Optional, only if you want absolute positioning
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the image to fill the entire panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}

public class LoginView extends JFrame{
	private JTextField txtUsername;
	private JTextField txtPassword;
	public LoginView() {
		getContentPane().setLayout(null);
		
		BackgroundPanel outerPanel = new BackgroundPanel("src/images/bg.png"); // Use full path or place in resources
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		outerPanel.setBounds(0, 0, screenSize.width, screenSize.height);
		getContentPane().add(outerPanel);
		outerPanel.setLayout(null);
//		JPanel outerPanel = new JPanel();
//		outerPanel.setBackground(new Color(128, 64, 64));
//		outerPanel.setBounds(0, 0, 717, 429);
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		outerPanel.setBounds(0, 0, screenSize.width, screenSize.height);
//		getContentPane().add(outerPanel);
//		outerPanel.setLayout(null);
		
		JPanel innerPanel = new JPanel();
//		innerPanel.setBounds(253, 115, 254, 281);
//		outerPanel.add(innerPanel);
//		innerPanel.setLayout(null);
		int innerPanelWidth = 254;
		int innerPanelHeight = 281;
		int innerPanelX = (screenSize.width - innerPanelWidth) / 2; // Center horizontally
		int innerPanelY = 115; // Keep your original Y value or adjust as needed
		innerPanel.setBounds(innerPanelX, innerPanelY, innerPanelWidth, innerPanelHeight);
		outerPanel.add(innerPanel);
		innerPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(26, 73, 212, 14);
		innerPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setBounds(95, 23, 46, 14);
		innerPanel.add(lblNewLabel);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(26, 98, 212, 20);
		innerPanel.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setBounds(26, 139, 212, 14);
		innerPanel.add(lblNewLabel_1_1);
		
		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(26, 164, 212, 20);
		innerPanel.add(txtPassword);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtUsername.getText().trim().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Username is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
					txtUsername.requestFocus(true);
					txtUsername.selectAll();
				}
				else if(txtPassword.getText().trim().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Password is a blank field!","Fail", JOptionPane.ERROR_MESSAGE);
					txtPassword.requestFocus(true);
					txtPassword.selectAll();
				}
				else
				{
					EmployeeController ec=new EmployeeController();
					EmployeeModel em=new EmployeeModel();
					em.setEmployee_name(txtUsername.getText().toString());
					em.setPassword(txtPassword.getText().toString());
					try {
						System.out.println("---------");
						if(ec.loginState(em))
						{
							
							System.out.println(ec.loginState(em));
							dispose();
							ProjectView pjv = new ProjectView();
							pjv.show();
						}
						else
						{
							System.out.println("There is no data ---------");
							JOptionPane.showMessageDialog(null, "Wrong username of password","Fail", JOptionPane.ERROR_MESSAGE);
							txtUsername.requestFocus(true);
							txtUsername.selectAll();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnNewButton.setBounds(26, 209, 212, 23);
		innerPanel.add(btnNewButton);
		
		JLabel lblNewLabel_2 = new JLabel("Project Management System");
		lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblNewLabel_2.setBounds(283, 26, 205, 31);
		outerPanel.add(lblNewLabel_2);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LoginView loginFrame = new LoginView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			loginFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			loginFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			loginFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
