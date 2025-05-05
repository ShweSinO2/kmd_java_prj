package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import controller.EmployeeController;
import model.EmployeeModel;

import javax.swing.BorderFactory;
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
		innerPanel.setBackground(new Color(255, 255, 255, 180)); // semi-transparent white
		innerPanel.setBorder(new LineBorder(new Color(255, 255, 255), 2, true)); 
		int innerPanelWidth = 284;
		int innerPanelHeight = 281;
		int innerPanelX = (screenSize.width - innerPanelWidth) / 2;
		int innerPanelY = 115;
		innerPanel.setBounds(541, 115, 294, 297);
		outerPanel.add(innerPanel);
		innerPanel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(26, 73, 212, 14);
		innerPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblNewLabel.setBounds(121, 23, 48, 14);
		innerPanel.add(lblNewLabel);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(26, 98, 242, 28);
		txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
		txtUsername.setBorder(BorderFactory.createCompoundBorder(
		    new LineBorder(new Color(200, 150, 100), 1, true),
		    BorderFactory.createEmptyBorder(5, 10, 5, 10)
		));
		txtUsername.setColumns(10);
		innerPanel.add(txtUsername);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setBounds(26, 139, 212, 14);
		innerPanel.add(lblNewLabel_1_1);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(26, 164, 242, 28);
		txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
		txtPassword.setBorder(BorderFactory.createCompoundBorder(
		    new LineBorder(new Color(200, 150, 100), 1, true),
		    BorderFactory.createEmptyBorder(5, 10, 5, 10)
		));
		innerPanel.add(txtPassword);
		txtPassword.setColumns(10);
		innerPanel.add(txtPassword);
		
		JButton btnNewButton = new JButton("Login");
		
		btnNewButton.setBackground(new Color(153, 76, 0));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setFont(new Font("SansSerif", Font.BOLD, 16));
		btnNewButton.setBorder(new LineBorder(new Color(102, 51, 0), 1, true));
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
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
							JOptionPane.showMessageDialog(null, "Wrong username or password","Fail", JOptionPane.ERROR_MESSAGE);
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
		btnNewButton.setBounds(26, 220, 242, 28);
		innerPanel.add(btnNewButton);
		
//		JLabel lblNewLabel_2 = new JLabel("Project Management System");
//		lblNewLabel_2.setFont(new Font("SansSerif", Font.PLAIN, 16));
//		lblNewLabel_2.setBounds(283, 26, 205, 31);
//		outerPanel.add(lblNewLabel_2);
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
