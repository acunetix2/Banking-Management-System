package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.model.Account;
import com.bankingsystem.bankingsystem.service.BankService;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
import java.awt.*;

public class Bank extends JFrame {

    private JTextField txtAccNo, txtName, txtAmount, txtType;
    private JTextArea output;
    private BankService service;
    private ConfigurableApplicationContext applicationContext;
    private String currentUsername;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color PANEL_BG = new Color(245, 247, 250);

    public Bank(BankService service, ConfigurableApplicationContext context) {
        this(service, context, null);
    }

    public Bank(BankService service, ConfigurableApplicationContext context, String username) {
        this.service = service;
        this.applicationContext = context;
        this.currentUsername = username;

        String titleText = "Banking System" + (username != null ? " - " + username : "");
        setTitle(titleText);
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                applicationContext.close();
                System.exit(0);
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(PANEL_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Bank Management System", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_COLOR);
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtAccNo = new JTextField();
        txtType = new JTextField();
        txtName = new JTextField();
        txtAmount = new JTextField();

        addField(formPanel, gbc, "Account No:", txtAccNo, 0);
        addField(formPanel, gbc, "Account Type:", txtType, 1);
        addField(formPanel, gbc, "Customer Name:", txtName, 2);
        addField(formPanel, gbc, "Amount:", txtAmount, 3);

        JButton btnCreate = createButton("Create Account");
        JButton btnDeposit = createButton("Deposit");
        JButton btnWithdraw = createButton("Withdraw");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnDeposit);
        buttonPanel.add(btnWithdraw);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        output = new JTextArea(6, 30);
        output.setEditable(false);
        output.setFont(new Font("Consolas", Font.PLAIN, 13));
        output.setBackground(new Color(245, 245, 245));
        output.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Transaction Log"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        btnCreate.addActionListener(e -> createAccount());
        btnDeposit.addActionListener(e -> deposit());
        btnWithdraw.addActionListener(e -> withdraw());
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(FIELD_FONT);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        field.setFont(FIELD_FONT);
        panel.add(field, gbc);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(PRIMARY_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private void createAccount() {
        if (txtAccNo.getText().isEmpty() || txtName.getText().isEmpty() ||
            txtType.getText().isEmpty() || txtAmount.getText().isEmpty()) {
            output.append("All fields are required!\n");
            return;
        }

        try {
            int accNo = Integer.parseInt(txtAccNo.getText());
            String name = txtName.getText();
            String type = txtType.getText();
            double amount = Double.parseDouble(txtAmount.getText());

            Account acc = new Account(accNo, type, name, amount);
            output.append(service.createAccount(acc) + "\n");
            autoScroll();

        } catch (NumberFormatException e) {
            output.append("Invalid numeric input!\n");
        }
    }

    private void deposit() {
        if (txtAccNo.getText().isEmpty() || txtAmount.getText().isEmpty()) {
            output.append("Account No and Amount are required!\n");
            return;
        }

        try {
            int accNo = Integer.parseInt(txtAccNo.getText());
            double amount = Double.parseDouble(txtAmount.getText());
            output.append(service.deposit(accNo, amount) + "\n");
            autoScroll();
        } catch (NumberFormatException e) {
            output.append("Invalid numeric input!\n");
        }
    }

    private void withdraw() {
        if (txtAccNo.getText().isEmpty() || txtAmount.getText().isEmpty()) {
            output.append("Account No and Amount are required!\n");
            return;
        }

        try {
            int accNo = Integer.parseInt(txtAccNo.getText());
            double amount = Double.parseDouble(txtAmount.getText());
            output.append(service.withdraw(accNo, amount) + "\n");
            autoScroll();
        } catch (NumberFormatException e) {
            output.append("Invalid numeric input!\n");
        }
    }

    private void autoScroll() {
        output.setCaretPosition(output.getDocument().getLength());
    }
}

