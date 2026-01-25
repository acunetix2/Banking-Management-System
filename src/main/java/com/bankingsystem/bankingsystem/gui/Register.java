package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.UserService;
import javax.swing.*;
import java.awt.*;

public class Register extends JDialog {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JComboBox<String> cmbRole;
    private UserService userService;

    public Register(UserService userService, JFrame parent) {
        super(parent, "Create New Account", true);
        this.userService = userService;

        setSize(420, 340);
        setLocationRelativeTo(parent);
        setResizable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridLayout(7, 2, 10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        main.setBackground(new Color(240, 240, 240));

        main.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        main.add(txtUsername);

        main.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        main.add(txtPassword);

        main.add(new JLabel("Confirm Password:"));
        txtConfirmPassword = new JPasswordField();
        main.add(txtConfirmPassword);

        main.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        main.add(txtEmail);

        main.add(new JLabel("Full Name:"));
        txtFullName = new JTextField();
        main.add(txtFullName);

        main.add(new JLabel("Role:"));
        cmbRole = new JComboBox<>(new String[]{"TELLER", "AOO"});
        main.add(cmbRole);

        JButton btnRegister = styleButton(new JButton("Register"), new Color(76, 175, 80));
        btnRegister.addActionListener(e -> registerUser());
        main.add(btnRegister);

        JButton btnCancel = styleButton(new JButton("Cancel"), new Color(150, 150, 150));
        btnCancel.addActionListener(e -> dispose());
        main.add(btnCancel);

        add(main);
    }

    private void registerUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();
        String role = (String) cmbRole.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (password.length() < 4) {
            JOptionPane.showMessageDialog(this, "Password must be at least 4 characters long.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            userService.registerUser(username, password, email, fullName, role);
            int result = JOptionPane.showConfirmDialog(this, 
                "Account registered successfully!\n\nUsername: " + username + "\nRole: " + role + "\n\nClick OK to close.", 
                "Registration Successful", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.INFORMATION_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                dispose();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton styleButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(Math.max(0, bgColor.getRed() - 15), Math.max(0, bgColor.getGreen() - 15), Math.max(0, bgColor.getBlue() - 15)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }
}
