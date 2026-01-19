package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Register extends JDialog {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JTextField txtFullName;
    private JComboBox<String> cmbRole;
    private JLabel lblMessage;
    private UserService userService;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SIDEBAR_COLOR = new Color(25, 45, 85);
    private final Color BUTTON_COLOR = new Color(76, 175, 80);
    private final Color CANCEL_COLOR = new Color(158, 158, 158);
    private final Color ERROR_COLOR = new Color(244, 67, 54);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color PANEL_BG = new Color(245, 247, 250);

    public Register(UserService userService, JFrame parent) {
        super(parent, "Create New Account", true);
        this.userService = userService;

        setSize(550, 700);
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createUI();
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeaderPanel();

        // Scroll panel for form
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Main layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SIDEBAR_COLOR);
        headerPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Fill in your details to register");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Form fields
        formPanel.add(createLabeledField("Username", txtUsername = new JTextField(), "Enter a unique username"));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledField("Password", txtPassword = new JPasswordField(), "Minimum 4 characters"));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledField("Confirm Password", txtConfirmPassword = new JPasswordField(), "Must match password"));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledField("Email", txtEmail = new JTextField(), "Enter a valid email"));
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(createLabeledField("Full Name", txtFullName = new JTextField(), "Enter your full name"));
        formPanel.add(Box.createVerticalStrut(15));

        // Role selector
        JPanel rolePanel = new JPanel(new BorderLayout());
        rolePanel.setBackground(Color.WHITE);
        rolePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel roleLabel = new JLabel("Account Role");
        roleLabel.setFont(LABEL_FONT);
        roleLabel.setForeground(SIDEBAR_COLOR);
        
        JLabel roleHintLabel = new JLabel("Select your role type");
        roleHintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        roleHintLabel.setForeground(new Color(120, 120, 120));
        
        JPanel roleLabelPanel = new JPanel(new BorderLayout());
        roleLabelPanel.setBackground(Color.WHITE);
        roleLabelPanel.add(roleLabel, BorderLayout.WEST);
        roleLabelPanel.add(roleHintLabel, BorderLayout.EAST);
        
        cmbRole = new JComboBox<>(new String[]{"Account Opening Officer (AOO)", "Teller (TELLER)"});
        cmbRole.setFont(FIELD_FONT);
        cmbRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cmbRole.setBackground(Color.WHITE);
        
        rolePanel.add(roleLabelPanel, BorderLayout.NORTH);
        rolePanel.add(Box.createVerticalStrut(8), BorderLayout.BEFORE_LINE_BEGINS);
        rolePanel.add(cmbRole, BorderLayout.SOUTH);
        
        formPanel.add(rolePanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Message label
        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(lblMessage);
        formPanel.add(Box.createVerticalStrut(20));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnRegister = createButton("Register", BUTTON_COLOR);
        btnRegister.addActionListener(e -> registerUser());
        
        JButton btnCancel = createButton("Cancel", CANCEL_COLOR);
        btnCancel.addActionListener(e -> dispose());
        
        buttonPanel.add(btnRegister);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(btnCancel);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalGlue());

        return formPanel;
    }

    private JPanel createLabeledField(String label, JTextField field, String hint) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(Color.WHITE);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(SIDEBAR_COLOR);
        
        JLabel hintLabel = new JLabel(hint);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        hintLabel.setForeground(new Color(120, 120, 120));
        
        labelPanel.add(lbl, BorderLayout.WEST);
        labelPanel.add(hintLabel, BorderLayout.EAST);

        field.setFont(FIELD_FONT);
        field.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        field.setPreferredSize(new Dimension(0, 38));

        panel.add(labelPanel, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(6), BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(field, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setPreferredSize(new Dimension(120, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(
                    Math.max(0, bgColor.getRed() - 15),
                    Math.max(0, bgColor.getGreen() - 15),
                    Math.max(0, bgColor.getBlue() - 15)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void registerUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String email = txtEmail.getText().trim();
        String fullName = txtFullName.getText().trim();
        String roleDisplay = (String) cmbRole.getSelectedItem();
        String role = roleDisplay.contains("AOO") ? "AOO" : "TELLER";

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
}
