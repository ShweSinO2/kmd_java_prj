package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.JDialog;

public class LoginNewView extends JFrame{
	public LoginNewView() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 128, 0));
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(128, 128, 64));
		panel_1.setPreferredSize(new Dimension(30, 30));
		panel.add(panel_1, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			LoginNewView loginFrame = new LoginNewView();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			loginFrame.setBounds(0, 0, screenSize.width, screenSize.height);
			loginFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			loginFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
