package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;
import com.bankingsystem.bankingsystem.service.UserService;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Login extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblMessage;
    private UserService userService;
    private ConfigurableApplicationContext applicationContext;
    private BankService bankService;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SIDEBAR_COLOR = new Color(25, 45, 85);
    private final Color PANEL_BG = new Color(245, 247, 250);
    private final Color ERROR_COLOR = new Color(244, 67, 54);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);

    public Login(UserService userService, ConfigurableApplicationContext context, BankService bankService) {
        this.userService = userService;
        this.applicationContext = context;
        this.bankService = bankService;

        setTitle("Banking System - Login");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Left side - Brand panel
        JPanel leftPanel = createLeftPanel();
        
        // Right side - Login form
        JPanel rightPanel = createRightPanel();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(SIDEBAR_COLOR);
        leftPanel.setPreferredSize(new Dimension(300, 0));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SIDEBAR_COLOR);
        contentPanel.setBorder(new EmptyBorder(60, 30, 30, 30));

        JLabel bankIcon = new JLabel("🏦");
        bankIcon.setFont(new Font("Segoe UI", Font.BOLD, 50));
        bankIcon.setForeground(PRIMARY_COLOR);
        bankIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(bankIcon);
        contentPanel.add(Box.createVerticalStrut(20));

        JLabel brandLabel = new JLabel("SecureBank");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(brandLabel);
        contentPanel.add(Box.createVerticalStrut(10));

        JLabel taglineLabel = new JLabel("We make banking secure and easy.");
        taglineLabel.setFont(SUBTITLE_FONT);
        taglineLabel.setForeground(new Color(200, 200, 200));
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(taglineLabel);
        contentPanel.add(Box.createVerticalStrut(40));

        String[] features = {
            "Alto Banking System"
            "Secure Transactions",
            "Fast Transfers",
            "24/7 Customer Support"
        };
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            featureLabel.setForeground(new Color(180, 180, 180));
            featureLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(featureLabel);
        }

        contentPanel.add(Box.createVerticalGlue());

        leftPanel.add(contentPanel, BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(PANEL_BG);
        rightPanel.setBorder(new EmptyBorder(60, 50, 60, 50));

        JLabel welcomeLabel = new JLabel("Welcome Back");
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(SIDEBAR_COLOR);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        JLabel signInLabel = new JLabel("Sign in to your account");
        signInLabel.setFont(SUBTITLE_FONT);
        signInLabel.setForeground(new Color(120, 120, 120));
        signInLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(signInLabel);
        rightPanel.add(Box.createVerticalStrut(30));

        // Username field
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUsername.setForeground(SIDEBAR_COLOR);
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(lblUsername);
        rightPanel.add(Box.createVerticalStrut(5));

        txtUsername = createTextField();
        rightPanel.add(txtUsername);
        rightPanel.add(Box.createVerticalStrut(15));

        // Password field
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPassword.setForeground(SIDEBAR_COLOR);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(lblPassword);
        rightPanel.add(Box.createVerticalStrut(5));

        txtPassword = new JPasswordField();
        txtPassword.setFont(FIELD_FONT);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginUser();
                }
            }
        });
        rightPanel.add(txtPassword);
        rightPanel.add(Box.createVerticalStrut(8));

        // Message label
        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(lblMessage);
        rightPanel.add(Box.createVerticalStrut(15));

        // Buttons
        JButton btnLogin = createButton("Sign In", PRIMARY_COLOR);
        btnLogin.addActionListener(e -> loginUser());
        rightPanel.add(btnLogin);
        rightPanel.add(Box.createVerticalStrut(12));

        JButton btnRegister = createButton("Create New Account", new Color(100, 100, 100));
        btnRegister.addActionListener(e -> openRegisterDialog());
        rightPanel.add(btnRegister);
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(FIELD_FONT);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txt.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        return txt;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(
                    Math.max(0, bgColor.getRed() - 20),
                    Math.max(0, bgColor.getGreen() - 20),
                    Math.max(0, bgColor.getBlue() - 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void loginUser() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userService.authenticateUser(username, password)) {
            int response = JOptionPane.showConfirmDialog(this, 
                "Login successful!\n\nClick OK to continue to Dashboard.", 
                "Login Success", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.INFORMATION_MESSAGE);
            
            if (response == JOptionPane.OK_OPTION) {
                var user = userService.getUserByUsername(username);
                if (user.isPresent()) {
                    SwingUtilities.invokeLater(() -> {
                        Dashboard dashboard = new Dashboard(bankService, userService, user.get(), applicationContext);
                        dashboard.setVisible(true);
                        dispose();
                    });
                }
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

    private void showError(String message) {
        lblMessage.setForeground(ERROR_COLOR);
        lblMessage.setText(message);
    }

    private void showSuccess(String message) {
        lblMessage.setForeground(SUCCESS_COLOR);
        lblMessage.setText(message);
    }
}
