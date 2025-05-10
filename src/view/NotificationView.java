package view;

import javax.swing.JDialog;
import javax.swing.*;

public class NotificationView extends JFrame{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			NotificationView notiFrame = new NotificationView();
			notiFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			notiFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			notiFrame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
