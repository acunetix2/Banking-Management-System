package com.bankingsystem.bankingsystem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.springframework.context.ConfigurableApplicationContext;

import com.bankingsystem.bankingsystem.model.User;
import com.bankingsystem.bankingsystem.service.BankService;
import com.bankingsystem.bankingsystem.service.UserService;

public class Dashboard extends JFrame {
    private BankService bankService;
    private UserService userService;
    private User user;
    private ConfigurableApplicationContext context;
    private JPanel contentPanel;

    private static final Color PRIMARY = new Color(33, 150, 243);
    private static final Color SIDEBAR = new Color(25, 118, 210);
    private static final Color BG = new Color(240, 240, 240);
    private static final Color SUCCESS = new Color(76, 175, 80);
    private static final Color INFO = new Color(33, 150, 243);
    private static final Color WARNING = new Color(255, 152, 0);
    private static final Color DANGER = new Color(244, 67, 54);
    private static final Font SIDEBAR_F = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font TITLE_F = new Font("Segoe UI", Font.BOLD, 24);

    public Dashboard(BankService bankService, User user, ConfigurableApplicationContext context) {
        this.bankService = bankService;
        this.user = user;
        this.context = context;

        setTitle("Banking System - Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createUI();
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent e) {
                context.close();
                System.exit(0);
            }
        });
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);
        main.add(createSidebar(), BorderLayout.WEST);
        
        JPanel top = new JPanel(new BorderLayout());
        top.add(createHeader(), BorderLayout.NORTH);
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(BG);
        loadContent();
        top.add(contentPanel, BorderLayout.CENTER);
        
        main.add(top, BorderLayout.CENTER);
        add(main);
    }

    private JPanel createSidebar() {
        JPanel s = new JPanel();
        s.setBackground(SIDEBAR);
        s.setPreferredSize(new Dimension(250, 0));
        s.setLayout(new BoxLayout(s, BoxLayout.Y_AXIS));
        s.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel logo = new JLabel("ALTO BANKING SYSTEM");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 20, 30, 20));
        s.add(logo);

        String role = user.getRole();
        s.add(btn("Dashboard", this::loadContent));
        
        if (role.equals("AOO")) {
            s.add(btn("Create Account", () -> openWindow(() -> new CreateAccount(bankService, user).setVisible(true))));
            s.add(btn("View Account", () -> openWindow(() -> new ViewAccount(bankService).setVisible(true))));
        } else if (role.equals("TELLER")) {
            s.add(btn("Transfer Money", () -> openWindow(() -> new Transfer(bankService).setVisible(true))));
            s.add(btn("View Account", () -> openWindow(() -> new ViewAccount(bankService).setVisible(true))));
            s.add(btn("Deposit", () -> openWindow(() -> new Deposit(bankService).setVisible(true))));
            s.add(btn("Withdraw", () -> openWindow(() -> new Withdraw(bankService).setVisible(true))));
        } else {
            s.add(btn("View Account", () -> openWindow(() -> new ViewAccount(bankService).setVisible(true))));
            s.add(btn("Deposit", () -> openWindow(() -> new Deposit(bankService).setVisible(true))));
            s.add(btn("Withdraw", () -> openWindow(() -> new Withdraw(bankService).setVisible(true))));
            s.add(btn("Transfer", () -> openWindow(() -> new Transfer(bankService).setVisible(true))));
        }

        s.add(Box.createVerticalGlue());
        s.add(btn("Logout", this::logout));
        return s;
    }

    private JButton btn(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(SIDEBAR_F);
        btn.setBackground(SIDEBAR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(15, 20, 15, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(PRIMARY); }
            public void mouseExited(java.awt.event.MouseEvent e) { btn.setBackground(SIDEBAR); }
        });
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private void openWindow(Runnable action) {
        try { action.run(); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private JPanel createHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(PRIMARY);
        h.setPreferredSize(new Dimension(0, 85));
        h.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel left = new JPanel(new BorderLayout(10, 5));
        left.setBackground(PRIMARY);
        
        JLabel title = new JLabel("Welcome, " + user.getFullName());
        title.setFont(TITLE_F);
        title.setForeground(Color.WHITE);
        
        String roleText = user.getRole().equals("AOO") ? "Account Operations Officer" : 
                         user.getRole().equals("TELLER") ? "Teller" : "Customer";
        JLabel role = new JLabel("Role: " + roleText);
        role.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        role.setForeground(new Color(200, 220, 255));
        
        left.add(title, BorderLayout.NORTH);
        left.add(role, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setBackground(PRIMARY);
        
        JButton profile = hdrBtn("Profile");
        profile.addActionListener(e -> showProfile());
        
        JButton settings = hdrBtn("Settings");
        settings.addActionListener(e -> showSettings());
        
        JButton logout = hdrBtn("Logout");
        logout.setBackground(DANGER);
        logout.addActionListener(e -> logout());
        
        right.add(profile);
        right.add(settings);
        right.add(logout);

        h.add(left, BorderLayout.WEST);
        h.add(right, BorderLayout.EAST);
        return h;
    }

    private JButton hdrBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(13, 110, 200));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(25, 130, 220)); }
            public void mouseExited(java.awt.event.MouseEvent e) { if (!btn.getBackground().equals(DANGER)) btn.setBackground(new Color(13, 110, 200)); }
        });
        return btn;
    }

    private void loadContent() {
        contentPanel.removeAll();
        JPanel scroll = new JPanel(new GridLayout(2, 2, 20, 20));
        scroll.setBackground(BG);
        scroll.setBorder(new EmptyBorder(30, 30, 30, 30));

        String role = user.getRole();
        if (role.equals("AOO")) {
            scroll.add(card("Create Account", "Create a bank account", 
                () -> openWindow(() -> new CreateAccount(bankService, user).setVisible(true)), SUCCESS, "[+]"));
            scroll.add(card("View Account", "Check account information",
                () -> openWindow(() -> new ViewAccount(bankService).setVisible(true)), PRIMARY, "[i]"));

        } else if (role.equals("TELLER")) {
            scroll.add(card("Transfer Money", "Transfer funds between\ncustomer accounts",
                () -> openWindow(() -> new Transfer(bankService).setVisible(true)), INFO, "[>]"));
            scroll.add(card("View Account", "Check account details\nand balances",
                () -> openWindow(() -> new ViewAccount(bankService).setVisible(true)), PRIMARY, "[i]"));
            scroll.add(card("Deposit Money", "Deposit funds into\ncustomer accounts",
                () -> openWindow(() -> new Deposit(bankService).setVisible(true)), SUCCESS, "[+]"));
            scroll.add(card("Withdraw Money", "Withdraw funds from\ncustomer accounts",
                () -> openWindow(() -> new Withdraw(bankService).setVisible(true)), DANGER, "[-]"));
        }

        JScrollPane sp = new JScrollPane(scroll);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG);
        contentPanel.add(sp, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel card(String title, String desc, Runnable action, Color color, String icon) {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        c.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel bar = new JPanel();
        bar.setBackground(color);
        bar.setPreferredSize(new Dimension(0, 8));

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(Color.WHITE);
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLbl.setForeground(color);
        titlePanel.add(iconLbl);
        titlePanel.add(titleLbl);

        JLabel descLbl = new JLabel("<html>" + desc + "</html>");
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLbl.setForeground(new Color(100, 100, 100));

        JButton actionBtn = new JButton("Open");
        actionBtn.setBackground(color);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        actionBtn.setFocusPainted(false);
        actionBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        actionBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionBtn.addActionListener(e -> action.run());

        content.add(titlePanel, BorderLayout.NORTH);
        content.add(descLbl, BorderLayout.CENTER);
        content.add(actionBtn, BorderLayout.SOUTH);

        c.add(bar, BorderLayout.NORTH);
        c.add(content, BorderLayout.CENTER);
        return c;
    }

    private void showProfile() {
        JDialog d = new JDialog(this, "User Profile", true);
        d.setSize(500, 450);
        d.setLocationRelativeTo(this);
        d.setResizable(true);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);

        JPanel hdr = new JPanel();
        hdr.setBackground(PRIMARY);
        hdr.setPreferredSize(new Dimension(0, 60));
        JLabel hdrLbl = new JLabel("User Profile Information");
        hdrLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        hdrLbl.setForeground(Color.WHITE);
        hdr.add(hdrLbl);

        JPanel cont = new JPanel();
        cont.setBackground(BG);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.setBorder(new EmptyBorder(30, 30, 30, 30));

        addField(cont, "Username:", user.getUsername());
        addField(cont, "Email:", user.getEmail() != null ? user.getEmail() : "N/A");
        addField(cont, "Role:", user.getRole());

        cont.add(Box.createVerticalGlue());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(BG);
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(PRIMARY);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeBtn.addActionListener(e -> d.dispose());
        btnPanel.add(closeBtn);

        p.add(hdr, BorderLayout.NORTH);
        p.add(new JScrollPane(cont), BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);
        
        d.add(p);
        d.setVisible(true);
    }

    private void addField(JPanel p, String label, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JTextField fld = new JTextField(value);
        fld.setEditable(false);
        fld.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fld.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        fld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        p.add(lbl);
        p.add(Box.createVerticalStrut(5));
        p.add(fld);
        p.add(Box.createVerticalStrut(20));
    }

    private void showSettings() {
        JDialog d = new JDialog(this, "Settings", true);
        d.setSize(500, 400);
        d.setLocationRelativeTo(this);
        d.setResizable(true);

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG);

        JPanel hdr = new JPanel();
        hdr.setBackground(PRIMARY);
        hdr.setPreferredSize(new Dimension(0, 60));
        JLabel hdrLbl = new JLabel("Application Settings");
        hdrLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        hdrLbl.setForeground(Color.WHITE);
        hdr.add(hdrLbl);

        JPanel cont = new JPanel();
        cont.setBackground(BG);
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));
        cont.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Theme:");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(PRIMARY);
        cont.add(title);
        cont.add(Box.createVerticalStrut(10));
        
        JComboBox<String> combo = new JComboBox<>(new String[]{"Light", "Dark"});
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cont.add(combo);
        cont.add(Box.createVerticalStrut(30));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        btnPanel.setBackground(BG);
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(SUCCESS);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        saveBtn.addActionListener(e -> { JOptionPane.showMessageDialog(d, "Settings saved!"); d.dispose(); });
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(PRIMARY);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        closeBtn.addActionListener(e -> d.dispose());
        
        btnPanel.add(saveBtn);
        btnPanel.add(closeBtn);

        p.add(hdr, BorderLayout.NORTH);
        p.add(new JScrollPane(cont), BorderLayout.CENTER);
        p.add(btnPanel, BorderLayout.SOUTH);
        
        d.add(p);
        d.setVisible(true);
    }

    private void logout() {
        Login login = new Login(userService, context, bankService);
        login.setVisible(true);
        dispose();
    }
}
