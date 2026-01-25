package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;
import com.bankingsystem.bankingsystem.service.UserService;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private UserService userService;
    private ConfigurableApplicationContext applicationContext;
    private BankService bankService;

    public Login(UserService userService, ConfigurableApplicationContext context, BankService bankService) {
        this.userService = userService;
        this.applicationContext = context;
        this.bankService = bankService;

        setTitle("Banking System - Login");
        setSize(700, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel main = new JPanel(new BorderLayout());
        main.add(createLeftPanel(), BorderLayout.WEST);
        main.add(createRightPanel(), BorderLayout.CENTER);
        add(main);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(25, 45, 85));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel title = new JLabel("ALTO  SYSTEMS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));

        JLabel subtitle = new JLabel("Banking Made Easy");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitle);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel welcome = new JLabel("Welcome Back");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcome.setForeground(new Color(25, 45, 85));
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(welcome);
        panel.add(Box.createVerticalStrut(5));

        JLabel signin = new JLabel("Sign in to your account");
        signin.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signin.setForeground(new Color(120, 120, 120));
        signin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(signin);
        panel.add(Box.createVerticalStrut(30));

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setForeground(new Color(25, 45, 85));
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblUsername);
        panel.add(Box.createVerticalStrut(5));

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(txtUsername);
        panel.add(Box.createVerticalStrut(15));

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(new Color(25, 45, 85));
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPassword);
        panel.add(Box.createVerticalStrut(5));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(txtPassword);
        panel.add(Box.createVerticalStrut(25));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBackground(new Color(245, 247, 250));

        JButton btnLogin = styleButton(new JButton("Sign In"), new Color(33, 150, 243));
        btnLogin.setPreferredSize(new Dimension(120, 40));
        btnLogin.addActionListener(e -> loginUser());
        btnPanel.add(btnLogin);
        btnPanel.add(Box.createHorizontalStrut(15));

        JButton btnRegister = styleButton(new JButton("Register"), new Color(76, 175, 80));
        btnRegister.setPreferredSize(new Dimension(120, 40));
        btnRegister.addActionListener(e -> openRegisterDialog());
        btnPanel.add(btnRegister);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        panel.add(btnPanel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userService.authenticateUser(username, password)) {
            var user = userService.getUserByUsername(username);
            if (user.isPresent()) {
                SwingUtilities.invokeLater(() -> {
                    Dashboard dashboard = new Dashboard(bankService, user.get(), applicationContext);
                    JOptionPane.showMessageDialog(dashboard, "Login Successful", "Welcome back, " + user.get().getFullName(), JOptionPane.INFORMATION_MESSAGE);
                    dashboard.setVisible(true);
                    dispose();
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
        }
    }

    private void openRegisterDialog() {
        Register registerGUI = new Register(userService, this);
        registerGUI.setVisible(true);
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
