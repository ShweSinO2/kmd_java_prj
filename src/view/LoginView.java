package view;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import config.MySqlQuery;
import controller.EmployeeController;
import model.EmployeeModel;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

public class LoginView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MySqlQuery sqlquery = new MySqlQuery();
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JLabel lblUsernameError; // User name
	private JLabel lblPasswordError; // Password error

	public LoginView() {

		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
		} catch (Exception ex) {
			System.err.println("Failed to initialize FlatLaf");
		}
		setTitle("Project Management System");
		getContentPane().setLayout(null);

		BackgroundPanel outerPanel = new BackgroundPanel("src/images/bg.png"); // Use full path or place in resources
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		outerPanel.setBounds(0, 0, screenSize.width, screenSize.height);
		getContentPane().add(outerPanel);
		outerPanel.setLayout(null);
		
		JPanel innerPanel = new JPanel();
//		innerPanel.setBackground(new Color(255, 255, 255, 180)); // semi-transparent white
//		innerPanel.setBorder(new LineBorder(new Color(255, 255, 255), 2, true)); 
		int innerPanelWidth = 350;
		int innerPanelHeight = 400;
		int innerPanelX = (screenSize.width - innerPanelWidth) / 2;
		int innerPanelY = 115;
//		innerPanel.setBounds(565, 115, 294, 297);
		innerPanel.setBounds(innerPanelX, innerPanelY, innerPanelWidth, innerPanelHeight);
		outerPanel.add(innerPanel);
		innerPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(30, 80, 212, 14);
		innerPanel.add(lblNewLabel_1);
		
		// "LOGIN" label
		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 30, innerPanelWidth, 20);
		innerPanel.add(lblNewLabel);

		// lblUsernameError
		lblUsernameError = new JLabel("Username is required.");
		lblUsernameError.setForeground(new Color(255, 0, 0)); // အနီရောင်
		lblUsernameError.setBounds(30, 142, 242, 14);
		innerPanel.add(lblUsernameError);
		lblUsernameError.setVisible(false);

//		txtUsername = new JTextField();
		txtUsername = new PlaceholderTextField("Username");
		txtUsername.setBounds(30, 105, innerPanelWidth - 60, 35);
		txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
//		txtUsername.setBorder(BorderFactory.createCompoundBorder(
//		    new LineBorder(new Color(200, 150, 100), 1, true),
//		    BorderFactory.createEmptyBorder(5, 10, 5, 10)
//		));
		txtUsername.setColumns(10);
		innerPanel.add(txtUsername);

		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setBounds(30, 165, 212, 14);
		innerPanel.add(lblNewLabel_1_1);

		// lblPasswordError
		lblPasswordError = new JLabel("Password is required.");
		lblPasswordError.setForeground(new Color(255, 0, 0));
		 lblPasswordError.setBounds(30, 227, 242, 14);
		innerPanel.add(lblPasswordError);
		lblPasswordError.setVisible(false);

//		txtPassword = new JPasswordField();
		txtPassword = new PlaceholderPasswordField("Password");
		txtPassword.setBounds(30, 190, innerPanelWidth - 75, 35);
		txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
//		txtPassword.setBorder(BorderFactory.createCompoundBorder(
//		    new LineBorder(new Color(200, 150, 100), 1, true),
//		    BorderFactory.createEmptyBorder(5, 10, 5, 10)
//		));
		innerPanel.add(txtPassword);
		txtPassword.setColumns(10);
		innerPanel.add(txtPassword);

		// Eye toggle button
//		JButton toggleBtn = new JButton("\uD83D\uDC41");
//		toggleBtn.setBackground(new Color(64, 0, 64));
//		toggleBtn.setBounds(240, 164, 28, 28);
//		toggleBtn.setFocusable(false);
//		toggleBtn.setBorderPainted(false);
//		toggleBtn.setContentAreaFilled(false);
//		toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//		innerPanel.add(toggleBtn);
//
//		// Toggle show/hide password
//		toggleBtn.addActionListener(new ActionListener() {
//			private boolean showing = false;
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (showing) {
//					txtPassword.setEchoChar('•'); // hide
//				} else {
//					txtPassword.setEchoChar((char) 0); // show
//				}
//				showing = !showing;
//			}
//		});
		
		// Eye toggle button with ImageIcon
		ImageIcon eyeIcon = new ImageIcon(new ImageIcon("src/images/view.png").getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));
		ImageIcon eyeSlashIcon = new ImageIcon(new ImageIcon("src/images/hide.png").getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH));

		JButton toggleBtn = new JButton(eyeSlashIcon);
		toggleBtn.setBounds(innerPanelWidth - 40, 195, 24, 24);
		toggleBtn.setFocusable(false);
		toggleBtn.setBorderPainted(false);
		toggleBtn.setContentAreaFilled(false);
		toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		innerPanel.add(toggleBtn);

		// Action listener
		toggleBtn.addActionListener(new ActionListener() {
		    private boolean showing = false;
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        if (showing) {
		            txtPassword.setEchoChar('•'); // hide
		            toggleBtn.setIcon(eyeSlashIcon); // icon change
		        } else {
		            txtPassword.setEchoChar((char) 0); // show
		            toggleBtn.setIcon(eyeIcon); // icon change
		        }
		        showing = !showing;
		    }
		});

		// Add the "Forgot Password" link
		JLabel lblForgotPassword = new JLabel("Forgot password?");
		lblForgotPassword.setForeground(new Color(153, 76, 0));
		lblForgotPassword.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblForgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblForgotPassword.setBounds(innerPanelWidth - 160, 240, 160, 14);
		innerPanel.add(lblForgotPassword);

		// Make the label clickable
		lblForgotPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Open the new ForgotPasswordView
				new ForgetPasswordView().setVisible(true);
			}
		});

		JButton btnLogin = new JButton("Login");

//		btnLogin.setUI(new BasicButtonUI());
		btnLogin.setBackground(new Color(153, 76, 0));
//		btnLogin.setBackground(UIManager.getColor("Button.default.background"));
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
//		btnLogin.setBorder(new LineBorder(new Color(102, 51, 0), 1, true));
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setFocusPainted(false);
		btnLogin.setContentAreaFilled(true);
//		btnLogin.setBorderPainted(false); 

		// Prevent background from changing when clicked
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnLogin.setBackground(new Color(204, 102, 0));
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				btnLogin.setBackground(new Color(153, 76, 0)); // Reset to default
			}
		});

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblUsernameError.setVisible(false);
				lblPasswordError.setVisible(false);

				boolean hasError = false;

				// Username blank
				if (txtUsername.getText().trim().isEmpty()) {
					lblUsernameError.setVisible(true);
					txtUsername.requestFocus(true);
					txtUsername.selectAll();
					hasError = true;
				}

				// Password blank
				if (txtPassword.getText().trim().isEmpty()) {
					lblPasswordError.setVisible(true);
					if (!hasError) { // Username blank
						txtPassword.requestFocus(true);
						txtPassword.selectAll();
					}
					hasError = true;
				}

				//no error
				if (!hasError) {
					EmployeeController ec = new EmployeeController();
					EmployeeModel em = new EmployeeModel();
					String username = txtUsername.getText().toString();
					String password = txtPassword.getText().toString();
					em.setEmployee_name(username);
					em.setPassword(password);
					try {
						if (ec.loginState(em)) {
							String[] querySeeker = MySqlQuery.getLoginUser(em.getEmployee_name(), em.getPassword());
							System.out.println("______Login______");
							System.out.println(querySeeker[0]);
							System.out.println(querySeeker[1]);
							System.out.println("______Login______");

							ProjectView prjFrame = new ProjectView(username, password);
							prjFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
							prjFrame.setVisible(true);
							dispose();
						} else {
							JOptionPane.showMessageDialog(null, "Username or Password is incorrect!", "Fail",
									JOptionPane.ERROR_MESSAGE);
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
		btnLogin.setBounds(30, 280, innerPanelWidth - 60, 40);
		innerPanel.add(btnLogin);
	}
	
	// Custom JTextField with placeholder
	class PlaceholderTextField extends JTextField {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String placeholder;

	    public PlaceholderTextField(String placeholder) {
	        this.placeholder = placeholder;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if (getText().isEmpty() && ! (hasFocus())) {
	            Graphics2D g2 = (Graphics2D) g.create();
	            g2.setColor(Color.GRAY);
	            Font originalFont = getFont();
	            g2.setFont(originalFont.deriveFont(Font.PLAIN, originalFont.getSize() - 2)); 
	            g2.drawString(placeholder, getInsets().left, g2.getFontMetrics().getAscent() + (getHeight() - g2.getFontMetrics().getHeight()) / 2);
	            g2.dispose();
	        }
	    }
	}

	// Custom JPasswordField with placeholder
	class PlaceholderPasswordField extends JPasswordField {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String placeholder;

	    public PlaceholderPasswordField(String placeholder) {
	        this.placeholder = placeholder;
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if (getPassword().length == 0 && ! (hasFocus())) {
	            Graphics2D g2 = (Graphics2D) g.create();
	            g2.setColor(Color.GRAY);
	            Font originalFont = getFont();
	            g2.setFont(originalFont.deriveFont(Font.PLAIN, originalFont.getSize() - 2)); 
	            g2.drawString(placeholder, getInsets().left, g2.getFontMetrics().getAscent() + (getHeight() - g2.getFontMetrics().getHeight()) / 2);
	            g2.dispose();
	        }
	    }
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(new FlatDarkLaf()); // အမှောင် theme ကိုပြောင်း
			LoginView loginFrame = new LoginView();
			SwingUtilities.updateComponentTreeUI(loginFrame);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			loginFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			loginFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			loginFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
