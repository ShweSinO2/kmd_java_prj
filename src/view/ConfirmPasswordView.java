package view;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.*;

import config.Common;

public class ConfirmPasswordView extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane, fieldPanel;
    private JPasswordField txtNewPassword, txtConfirmPassword;
    private JLabel rfvAllFields;
    private String userEmail;

    // Icon placeholders
    private ImageIcon iconShow;
    private ImageIcon iconHide;


    // Password regex
    private final String passwordRegex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@$!%*#?&]).{7,}$";

    public ConfirmPasswordView(String email) {
        this.userEmail = email;

        setTitle("Reset Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 325);
        setLocationRelativeTo(null);
        setResizable(false); // Optional: fix window size

        loadIcons();

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        setContentPane(contentPane);

        addTitlePanel();
        addFormPanel();
    }

    private void loadIcons() {
        iconShow = new ImageIcon("../ojt_mdcr_recruitment_system/src/image/view.png");
        iconHide = new ImageIcon("../ojt_mdcr_recruitment_system/src/image/hide.png");

        iconShow = new ImageIcon(iconShow.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        iconHide = new ImageIcon(iconHide.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
    }

    private void addTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        titlePanel.setBackground(Color.BLACK);

        JLabel lblTitle = new JLabel("Reset Password");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBorder(new EmptyBorder(10, 10, 10, 10));

        titlePanel.add(lblTitle, BorderLayout.WEST);
        contentPane.add(titlePanel);
        contentPane.add(Box.createVerticalStrut(20));
    }

    private void addFormPanel() {
        fieldPanel = new JPanel(new GridBagLayout());
        fieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // New Password Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        fieldPanel.add(new JLabel("New Password:"), gbc);

        // New Password Field with toggle
        gbc.gridx = 1;
        JPanel newPassPanel = createPasswordFieldPanel(txtNewPassword = new JPasswordField(), "Enter new password", iconHide, iconShow);
        fieldPanel.add(newPassPanel, gbc);

        // Confirm Password Label
        gbc.gridx = 0;
        gbc.gridy = 1;
        fieldPanel.add(new JLabel("Confirm Password:"), gbc);

        // Confirm Password Field with toggle
        gbc.gridx = 1;
        gbc.insets = new Insets(10, 10, 0, 10);
        JPanel confirmPassPanel = createPasswordFieldPanel(txtConfirmPassword = new JPasswordField(), "Confirm new password", iconHide, iconShow);
        fieldPanel.add(confirmPassPanel, gbc);

        // Error label with fixed size to prevent layout jump
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 10, 0, 0);
        rfvAllFields = new JLabel(" ");
        rfvAllFields.setForeground(Color.RED);
        rfvAllFields.setPreferredSize(new Dimension(250, 40)); // Reserve space
        rfvAllFields.setMinimumSize(new Dimension(250, 40));
        rfvAllFields.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        fieldPanel.add(rfvAllFields, gbc);

        // Save button panel
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 10, 10);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSave = Common.addActionButton("Save");
        btnSave.setPreferredSize(new Dimension(130, 30));
        btnSave.addActionListener(e -> handleChangePassword());
        buttonPanel.add(btnSave);
        fieldPanel.add(buttonPanel, gbc);

        contentPane.add(fieldPanel);
        contentPane.add(Box.createVerticalGlue());
    }

    private JPanel createPasswordFieldPanel(JPasswordField passwordField, String placeholder, ImageIcon iconHide, ImageIcon iconShow) {
        JPanel panel = new JPanel(new BorderLayout());
        passwordField.setColumns(15);
        panel.setPreferredSize(new Dimension(250, 36));

        stylePasswordField(passwordField);
       // setPlaceholder(passwordField, placeholder);
        Common.setPlaceholder(passwordField, placeholder);
        passwordField.setEchoChar('*');

        JButton toggleBtn = new JButton(iconHide);
        toggleBtn.setPreferredSize(new Dimension(40, 30));
        toggleBtn.setFocusPainted(false);
        toggleBtn.setContentAreaFilled(true);
        toggleBtn.setBorderPainted(false);

        // Background color consistent regardless of toggle (customize if needed)
        Color bgColor = new Color(230, 230, 235);
        toggleBtn.setBackground(bgColor);

        toggleBtn.addActionListener(e -> {
            if (passwordField.getEchoChar() == 0) {
                // Hide password
                passwordField.setEchoChar('*');
                toggleBtn.setIcon(iconHide);
            } else {
                // Show password
                passwordField.setEchoChar((char) 0);
                toggleBtn.setIcon(iconShow);
            }
        });

        panel.add(passwordField, BorderLayout.CENTER);
        panel.add(toggleBtn, BorderLayout.EAST);

        return panel;
    }

    private void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
    }

    private void handleChangePassword() {
        String newPassword = String.valueOf(txtNewPassword.getPassword()).trim();
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword()).trim();

        // Check placeholders, avoid false positive from placeholders showing
        if (newPassword.equals("Enter new password") || confirmPassword.equals("Confirm new password")) {
            rfvAllFields.setText("Please fill in both fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            rfvAllFields.setText("Passwords do not match.");
            return;
        }

        if (!Pattern.matches(passwordRegex, newPassword)) {
            rfvAllFields.setText("<html>Password must be at least 7 length<br>& numbers & a special character.</html>");
            return;
        }

        if (updatePasswordInDatabase(newPassword)) {
            JOptionPane.showMessageDialog(this, "Password updated successfully.");
            new LoginView().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean updatePasswordInDatabase(String newPassword) {
        System.out.println("Attempting to update password for user: " + userEmail);
        System.out.println("New password: " + newPassword);

        String sql = "UPDATE useraccount SET user_password = ? WHERE user_email = ?";
        try (Connection conn = config.DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setString(1, newPassword);
            stmt.setString(2, userEmail);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    private void setPlaceholder(JPasswordField passwordField, String placeholder) {
        final char defaultEchoChar = '*';
        final boolean[] showingPlaceholder = {true};

        passwordField.setEchoChar((char) 0);
        passwordField.setForeground(Color.GRAY);
        passwordField.setText(placeholder);

        passwordField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (showingPlaceholder[0]) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                    passwordField.setEchoChar(defaultEchoChar);
                    showingPlaceholder[0] = false;
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText(placeholder);
                    passwordField.setForeground(Color.GRAY);
                    passwordField.setEchoChar((char) 0);
                    showingPlaceholder[0] = true;
                }
            }
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ConfirmPasswordView view = new ConfirmPasswordView("user@example.com");
            view.setVisible(true);
        });
    }
}
