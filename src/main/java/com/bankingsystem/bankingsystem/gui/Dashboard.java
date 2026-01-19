package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.model.User;
import com.bankingsystem.bankingsystem.service.BankService;
import com.bankingsystem.bankingsystem.service.UserService;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Dashboard extends JFrame {

    private BankService bankService;
    private UserService userService;
    private User loggedInUser;
    private ConfigurableApplicationContext applicationContext;
    private JPanel contentPanel;

    private final Color SIDEBAR_COLOR = new Color(25, 45, 85);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    private final Color INFO_COLOR = new Color(2, 136, 209);
    private final Color WARNING_COLOR = new Color(255, 152, 0);
    private final Color PANEL_BG = new Color(245, 247, 250);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font SIDEBAR_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    public Dashboard(BankService bankService, UserService userService, User user, ConfigurableApplicationContext context) {
        this.bankService = bankService;
        this.userService = userService;
        this.loggedInUser = user;
        this.applicationContext = context;

        setTitle("Banking System - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                applicationContext.close();
                System.exit(0);
            }
        });
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(PANEL_BG);

        // Create Sidebar
        JPanel sidebarPanel = createSidebar();

        // Create Header
        JPanel headerPanel = createHeader();

        // Create Content Area
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(PANEL_BG);
        loadDashboardContent();

        // Combine all panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(contentPanel, BorderLayout.CENTER);

        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(topPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_COLOR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel logoLabel = new JLabel("ALTO BANKING SYSTEM");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoLabel.setBorder(new EmptyBorder(0, 20, 30, 20));
        sidebar.add(logoLabel);

        // Menu Items
        if ("AOO".equals(loggedInUser.getRole())) {
            sidebar.add(createSidebarButton("Dashboard", () -> loadDashboardContent()));
            sidebar.add(createSidebarButton("Create Account", () -> new CreateAccount(bankService, loggedInUser).setVisible(true)));
            sidebar.add(createSidebarButton("Transfer Money", () -> new Transfer(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("View Account", () -> new ViewAccount(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("Account Operations", () -> new AccountOperations(bankService).setVisible(true)));
        } else if ("TELLER".equals(loggedInUser.getRole())) {
            sidebar.add(createSidebarButton("Dashboard", () -> loadDashboardContent()));
            sidebar.add(createSidebarButton("Transfer Money", () -> new Transfer(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("View Account", () -> new ViewAccount(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("Deposit", () -> new Deposit(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("Withdraw", () -> new Withdraw(bankService).setVisible(true)));
        } else if ("CUSTOMER".equals(loggedInUser.getRole())) {
            sidebar.add(createSidebarButton("Dashboard", () -> loadDashboardContent()));
            sidebar.add(createSidebarButton("View Account", () -> new ViewAccount(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("Deposit", () -> new Deposit(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("Withdraw", () -> new Withdraw(bankService).setVisible(true)));
            sidebar.add(createSidebarButton("Transfer", () -> new Transfer(bankService).setVisible(true)));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(createSidebarButton("Logout", this::logout));

        return sidebar;
    }

    private JButton createSidebarButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(SIDEBAR_FONT);
        btn.setBackground(SIDEBAR_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(15, 20, 15, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 50));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(33, 150, 243));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR_COLOR);
            }
        });
        
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(0, 85));
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        // Enhanced left panel with better styling
        JLabel titleLabel = new JLabel("Welcome, " + loggedInUser.getFullName());
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);

        JLabel roleLabel = new JLabel("Role: " + (loggedInUser.getRole() if == "AOO" ? "Account Operations Officer" : loggedInUser.getRole().equals("TELLER") ? "Teller" : "Customer"));
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(200, 220, 255));

        JPanel leftPanel = new JPanel(new BorderLayout(10, 5));
        leftPanel.setBackground(PRIMARY_COLOR);
        leftPanel.add(titleLabel, BorderLayout.NORTH);
        leftPanel.add(roleLabel, BorderLayout.CENTER);

        // Enhanced right panel with better button styling
        JButton btnProfile = createHeaderButton("Profile");
        btnProfile.addActionListener(e -> openProfileWindow());
        
        JButton btnSettings = createHeaderButton("Settings");
        btnSettings.addActionListener(e -> openSettingsWindow());
        
        JButton btnLogout = createHeaderButton("Logout");
        btnLogout.setBackground(DANGER_COLOR);
        btnLogout.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setBackground(PRIMARY_COLOR);
        rightPanel.add(btnProfile);
        rightPanel.add(btnSettings);
        rightPanel.add(btnLogout);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JButton createHeaderButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(13, 110, 200));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setRolloverEnabled(true);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(25, 130, 220));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getBackground().equals(new Color(25, 130, 220)) || 
                    btn.getBackground().equals(new Color(13, 110, 200))) {
                    btn.setBackground(new Color(13, 110, 200));
                }
            }
        });
        return btn;
    }

    private void loadDashboardContent() {
        contentPanel.removeAll();

        JPanel scrollContent = new JPanel(new GridLayout(2, 2, 20, 20));
        scrollContent.setBackground(PANEL_BG);
        scrollContent.setBorder(new EmptyBorder(30, 30, 30, 30));

        if ("AOO".equals(loggedInUser.getRole())) {
            scrollContent.add(createActionCard("Create Account", 
                "Create new bank accounts\nwith auto-generated numbers", 
                () -> new CreateAccount(bankService, loggedInUser).setVisible(true), 
                SUCCESS_COLOR, "[+]"));
            scrollContent.add(createActionCard("Transfer Money",
                "Transfer funds between\ncustomer accounts",
                () -> new Transfer(bankService).setVisible(true),
                INFO_COLOR, "[>]"));
            scrollContent.add(createActionCard("View Account",
                "Check account details\nand current balance",
                () -> new ViewAccount(bankService).setVisible(true),
                PRIMARY_COLOR, "[i]"));
            scrollContent.add(createActionCard("Account Operations",
                "Deposit, withdraw, or\nmanage accounts",
                () -> new AccountOperations(bankService).setVisible(true),
                WARNING_COLOR, "[*]"));
        } else if ("TELLER".equals(loggedInUser.getRole())) {
            scrollContent.add(createActionCard("Transfer Money",
                "Transfer funds between\ncustomer accounts",
                () -> new Transfer(bankService).setVisible(true),
                INFO_COLOR, "[>]"));
            scrollContent.add(createActionCard("View Account",
                "Check account details\nand balances",
                () -> new ViewAccount(bankService).setVisible(true),
                PRIMARY_COLOR, "[i]"));
            scrollContent.add(createActionCard("Deposit Money",
                "Deposit funds into\ncustomer accounts",
                () -> new Deposit(bankService).setVisible(true),
                SUCCESS_COLOR, "[+]"));
            scrollContent.add(createActionCard("Withdraw Money",
                "Withdraw funds from\ncustomer accounts",
                () -> new Withdraw(bankService).setVisible(true),
                DANGER_COLOR, "[-]"));
        }

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_BG);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createActionCard(String title, String description, Runnable action, Color color, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Top colored bar
        JPanel topBar = new JPanel();
        topBar.setBackground(color);
        topBar.setPreferredSize(new Dimension(0, 8));

        // Content area
        JPanel contentArea = new JPanel(new BorderLayout(10, 10));
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Icon and title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(color);
        
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        // Description
        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(100, 100, 100));

        // Button
        JButton actionBtn = new JButton("Open");
        actionBtn.setBackground(color);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setFocusPainted(false);
        actionBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionBtn.addActionListener(e -> action.run());

        contentArea.add(titlePanel, BorderLayout.NORTH);
        contentArea.add(descLabel, BorderLayout.CENTER);
        contentArea.add(actionBtn, BorderLayout.SOUTH);

        card.add(topBar, BorderLayout.NORTH);
        card.add(contentArea, BorderLayout.CENTER);

        return card;
    }

    private void openProfileWindow() {
        JDialog profileDialog = new JDialog(this, "User Profile", true);
        profileDialog.setSize(500, 450);
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel headerLabel = new JLabel("User Profile Information");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Content panel with profile info
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(PANEL_BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField usernameField = new JTextField(loggedInUser.getUsername());
        usernameField.setEditable(false);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        contentPanel.add(usernameLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(usernameField);
        contentPanel.add(Box.createVerticalStrut(20));

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField emailField = new JTextField(loggedInUser.getEmail() != null ? loggedInUser.getEmail() : "N/A");
        emailField.setEditable(false);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        contentPanel.add(emailLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(emailField);
        contentPanel.add(Box.createVerticalStrut(20));

        // Role
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField roleField = new JTextField(loggedInUser.getRole());
        roleField.setEditable(false);
        roleField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        roleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        contentPanel.add(roleLabel);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(roleField);
        contentPanel.add(Box.createVerticalStrut(30));

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(new Color(230, 245, 255));
        infoPanel.setBorder(BorderFactory.createLineBorder(INFO_COLOR, 1));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel infoTitle = new JLabel("[i] Account Information");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoTitle.setForeground(INFO_COLOR);
        infoPanel.add(infoTitle);
        infoPanel.add(Box.createVerticalStrut(8));

        JLabel infoText = new JLabel("<html>This is your profile information. " +
                "Contact the administrator if you need to update your details.</html>");
        infoText.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoText.setForeground(new Color(50, 50, 50));
        infoPanel.add(infoText);

        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(PANEL_BG);
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setBackground(PRIMARY_COLOR);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> profileDialog.dispose());
        buttonPanel.add(closeBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        profileDialog.add(mainPanel);
        profileDialog.setVisible(true);
    }

    private void openSettingsWindow() {
        JDialog settingsDialog = new JDialog(this, "Settings", true);
        settingsDialog.setSize(500, 500);
        settingsDialog.setLocationRelativeTo(this);
        settingsDialog.setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel headerLabel = new JLabel("Application Settings");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(PANEL_BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Display Settings Section
        JLabel displayTitle = new JLabel("Display Settings");
        displayTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        displayTitle.setForeground(PRIMARY_COLOR);
        contentPanel.add(displayTitle);
        contentPanel.add(Box.createVerticalStrut(12));

        // Theme setting
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        themePanel.setBackground(PANEL_BG);
        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String[] themes = {"Light", "Dark"};
        JComboBox<String> themeCombo = new JComboBox<>(themes);
        themeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        themePanel.add(themeLabel);
        themePanel.add(themeCombo);
        contentPanel.add(themePanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Security Settings Section
        JLabel securityTitle = new JLabel("Security Settings");
        securityTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        securityTitle.setForeground(PRIMARY_COLOR);
        contentPanel.add(securityTitle);
        contentPanel.add(Box.createVerticalStrut(12));

        // Change password button
        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        changePasswordBtn.setBackground(SUCCESS_COLOR);
        changePasswordBtn.setForeground(Color.WHITE);
        changePasswordBtn.setFocusPainted(false);
        changePasswordBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        changePasswordBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePasswordBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(settingsDialog,
                    "Change password functionality would be implemented here.",
                    "Change Password",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        contentPanel.add(changePasswordBtn);
        contentPanel.add(Box.createVerticalStrut(15));

        // About Section
        JLabel aboutTitle = new JLabel("About");
        aboutTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aboutTitle.setForeground(PRIMARY_COLOR);
        contentPanel.add(aboutTitle);
        contentPanel.add(Box.createVerticalStrut(12));

        JLabel appNameLabel = new JLabel("Application: ALTO BANKING SYSTEM");
        appNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        contentPanel.add(appNameLabel);

        JLabel versionLabel = new JLabel("Version: 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        contentPanel.add(versionLabel);

        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(PANEL_BG);
        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        saveBtn.setBackground(SUCCESS_COLOR);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(settingsDialog,
                    "Settings saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            settingsDialog.dispose();
        });

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeBtn.setBackground(PRIMARY_COLOR);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> settingsDialog.dispose());

        buttonPanel.add(saveBtn);
        buttonPanel.add(closeBtn);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        settingsDialog.add(mainPanel);
        settingsDialog.setVisible(true);
    }

    private void logout() {
        applicationContext.close();
        System.exit(0);
    }
}
