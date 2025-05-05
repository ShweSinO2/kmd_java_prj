package view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDialog;
import javax.swing.*;

public class AttachmentView extends JFrame{

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
