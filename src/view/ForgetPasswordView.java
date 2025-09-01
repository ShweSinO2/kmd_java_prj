package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import config.DBConfig;

import java.util.Properties;
import java.util.Random;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ForgetPasswordView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtEmail;
    private JTextField txtOtp;
    
    private Timer otpTimer;
    private int countdown = 60;
    private JButton btnGetOtp; 
    private boolean isOtpValid = false;

    private String generatedOtp;
    public static Connection conn = null;
    
	static {
		DBConfig cls = new DBConfig();
		try {
			conn = cls.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fail, Inter error", "Fail", JOptionPane.ERROR_MESSAGE);
		}

	}

    // mail credentials
    private final String senderEmail = "shwesinmyat7@gmail.com";
    private final String senderAppPassword = "bghqfrsopdccstct";

    public ForgetPasswordView() {
        setTitle("Forgot Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 300);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titlePanel.setBackground(new Color(255, 164, 92));
       

        JLabel lblTitle = new JLabel("Forgot Password");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridBagLayout());
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        txtEmail = new JTextField(20);
        styleTextField(txtEmail);
        fieldPanel.add(txtEmail, gbc);
        setPlaceholder(txtEmail, "Enter your email");

        gbc.gridx = 2;
        gbc.gridy = 0;
//        btnGetOtp = Common.addActionButton("Get OTP");
        btnGetOtp = new JButton("Get OTP");
        btnGetOtp.setPreferredSize(new Dimension(90, 30));
        btnGetOtp.setFont(new Font("Arial", Font.PLAIN, 12));
        fieldPanel.add(btnGetOtp, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblOtp = new JLabel("OTP:");
        lblOtp.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldPanel.add(lblOtp, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 10, 0, 10);
    	JLabel rfvEmail = new JLabel(" ");
    	rfvEmail.setForeground(Color.red);
    	fieldPanel.add(rfvEmail, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 0, 10);
        txtOtp = new JTextField(20);
        styleTextField(txtOtp);
        fieldPanel.add(txtOtp, gbc);
        gbc.gridwidth = 1;
        setPlaceholder(txtOtp, "Enter your OTP code");
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 0, 10);
    	JLabel rfvOtp = new JLabel(" ");
    	rfvOtp.setForeground(Color.red);
    	fieldPanel.add(rfvOtp, gbc);
        
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        JButton btnSubmit = Common.addActionButton("Submit");
        JButton btnSubmit = new JButton("Submit");

        btnSubmit.setFont(new Font("Arial", Font.PLAIN, 13));
        btnSubmit.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(btnSubmit);

        contentPane.add(titlePanel);
        contentPane.add(Box.createVerticalStrut(20));
        contentPane.add(fieldPanel);
        contentPane.add(Box.createVerticalStrut(10));
        contentPane.add(buttonPanel);
        contentPane.add(Box.createVerticalGlue());

        // OTP Generation with DB check
        btnGetOtp.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            if (email.equals("Enter your email")) {
            	rfvEmail.setText("Email is required.");
                return;
            }
            
            if (!isValidEmail(email)) {
            	rfvEmail.setText("Please enter a valid email address.");
//                JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isEmailRegistered(email)) {
            	rfvEmail.setText("This email is not registered in the system.");
//                JOptionPane.showMessageDialog(this, "This email is not registered in the system.", "Email Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            generatedOtp = generateOtp();
            boolean sent = sendOtpEmail(email, generatedOtp);
            if (sent) {
            	rfvEmail.setText(" ");
            	rfvOtp.setText(" ");
                JOptionPane.showMessageDialog(this, "OTP sent to " + email, "Success", JOptionPane.INFORMATION_MESSAGE);
                isOtpValid = true; // âœ… Mark OTP as valid
                startOtpTimer();   // ðŸ•’ Start cooldown and expiration timer
            } else {
                JOptionPane.showMessageDialog(this, "Failed to send OTP.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSubmit.addActionListener(e -> {
            String enteredOtp = txtOtp.getText().trim();

            if (generatedOtp == null) {
            	rfvOtp.setText("Please get the OTP first.");
//                JOptionPane.showMessageDialog(this, "Please get the OTP first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isOtpValid) {
            	rfvOtp.setText("OTP has expired. Please request a new one.");
//                JOptionPane.showMessageDialog(this, "OTP has expired. Please request a new one.", "Expired OTP", JOptionPane.ERROR_MESSAGE);
                txtOtp.setText("");
                return;
            }

            if (enteredOtp.equals(generatedOtp)) {
                JOptionPane.showMessageDialog(this, "OTP is correct! You can proceed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                ConfirmPasswordView confirmView = new ConfirmPasswordView(txtEmail.getText().trim());
                confirmView.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect OTP. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textField.setBackground(new Color(245, 245, 245));
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    private boolean isEmailRegistered(String email) {
        try {
            String sql = "SELECT COUNT(*) FROM employee WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private boolean sendOtpEmail(String recipientEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(props,
            new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderAppPassword);
                }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "[Task Management System]"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail));
            message.setSubject("Your OTP Code");
            
         // Create HTML content
            String htmlContent = "<!DOCTYPE html>" +
                    "<html lang='en'>" +
                    "<head>" +
                    "<meta charset='UTF-8'>" +
                    "<title>Email OTP</title>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f3f4f6; margin: 0; padding: 0; }" +
                    ".otp-container { " +
                    "  max-width: 600px; " +
                    "  margin: 50px auto; " +
                    "  background-color: white; " +
                    "  padding: 30px 40px; " +
                    "  border: 2px solid #ccc; " + /* Form border */
                    "  border-radius: 10px; " +
                    "  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); " +
                    "  text-align: center; " +
                    "}" +
                    ".otp-container h2 { color: #0066cc; margin-bottom: 20px; }" +
                    ".otp-code { " +
                    "  font-size: 32px; " +
                    "  color: #0066cc; " +
                    "  font-weight: bold; " +
                    "  margin: 20px auto; " +
                    "  padding: 10px 20px; " +
                    "  border: 2px solid #f3f4f6; " + /* OTP border */
                    "  background-color: #f0f8ff; " +
                    "  display: inline-block; " +
                    "  border-radius: 6px; " +
                    "}" +
                    ".footer { margin-top: 20px; font-size: 14px; color: #777; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='otp-container'>" +
                    "<h2>OTP</h2>" +
                    "<p><strong>Dear User,</strong></p>" +
                    "<p>Your One-Time Password (OTP) is:</p>" +
                    "<div class='otp-code'>" + otp + "</div>" +
                    "<p>Please use this code to complete your action. It is valid for a limited time.</p>" +
                    "<div class='footer'>" +
                    "<p>Thank you,<br>Task Management System</p>" +
                    "</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
 
            message.setContent(htmlContent, "text/html; charset=utf-8");
 
            //message.setText("Your OTP code is: " + otp);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void setPlaceholder(JTextField textField, String placeholder) {
        // Set initial placeholder
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);

        // To keep track of whether placeholder is currently shown
        final boolean[] showingPlaceholder = {true};

        // Focus behavior
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder[0]) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    showingPlaceholder[0] = false;
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                    showingPlaceholder[0] = true;
                }
            }
        });

        // Key listener â€” placeholder gets removed on first key press
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (showingPlaceholder[0]) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                    showingPlaceholder[0] = false;
                }
            }
        });

        // Document listener â€” placeholder reappears if text is cleared
        textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void update() {
                SwingUtilities.invokeLater(() -> {
                    if (!showingPlaceholder[0] && textField.getText().isEmpty()) {
                        textField.setForeground(Color.GRAY);
                        textField.setText(placeholder);
                        showingPlaceholder[0] = true;
                        // Move caret to start to simulate placeholder behavior
                        textField.setCaretPosition(0);
                    }
                });
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });
    }
    private void startOtpTimer() {
    	UIManager.put("Button.disabledText", Color.WHITE);
        btnGetOtp.setEnabled(false);
        btnGetOtp.setText("Wait 60s");

        countdown = 60;

        otpTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                countdown--;
                if (countdown > 0) {
                    btnGetOtp.setText("Wait " + countdown + "s");
                } else {
                    otpTimer.stop();
                    btnGetOtp.setEnabled(true);
                    btnGetOtp.setText("Get OTP");
                    isOtpValid = false; // â�Œ Invalidate OTP after time runs out
                }
            }
        });
        otpTimer.start();
    }



    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ForgetPasswordView view = new ForgetPasswordView();
            view.setVisible(true);
        });
    }
}


