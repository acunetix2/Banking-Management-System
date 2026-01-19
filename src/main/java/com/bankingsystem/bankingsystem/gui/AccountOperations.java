package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AccountOperations extends JDialog {

    private JTextField txtAccountNumber;
    private JTextField txtAmount;
    private JLabel lblMessage;
    private BankService bankService;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color DANGER_COLOR = new Color(244, 67, 54);
    private final Color ERROR_COLOR = new Color(244, 67, 54);

    public AccountOperations(BankService bankService) {
        this.bankService = bankService;

        setTitle("Account Operations");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Deposit or Withdraw");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel accountPanel = new JPanel(new BorderLayout(8, 0));
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setFont(FIELD_FONT);
        accLabel.setPreferredSize(new Dimension(130, 20));
        txtAccountNumber = new JTextField();
        txtAccountNumber.setFont(FIELD_FONT);
        JButton btnSearch = createButton("Search", PRIMARY_COLOR);
        btnSearch.setPreferredSize(new Dimension(80, 30));
        btnSearch.addActionListener(e -> searchAccount());
        accountPanel.add(accLabel, BorderLayout.WEST);
        accountPanel.add(txtAccountNumber, BorderLayout.CENTER);
        accountPanel.add(btnSearch, BorderLayout.EAST);
        mainPanel.add(accountPanel);
        mainPanel.add(createFieldPanel("Amount:", txtAmount = new JTextField()));

        mainPanel.add(Box.createVerticalStrut(20));

        lblMessage = new JLabel("");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(lblMessage);

        mainPanel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnDeposit = createButton("Deposit", SUCCESS_COLOR);
        btnDeposit.addActionListener(e -> deposit());

        JButton btnWithdraw = createButton("Withdraw", DANGER_COLOR);
        btnWithdraw.addActionListener(e -> withdraw());

        JButton btnCancel = createButton("Cancel", new Color(158, 158, 158));
        btnCancel.addActionListener(e -> dispose());

        buttonPanel.add(btnDeposit);
        buttonPanel.add(btnWithdraw);
        buttonPanel.add(btnCancel);
        mainPanel.add(buttonPanel);

        add(mainPanel);
    }

    private JPanel createFieldPanel(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel lbl = new JLabel(label);
        lbl.setFont(FIELD_FONT);
        lbl.setPreferredSize(new Dimension(130, 20));

        field.setFont(FIELD_FONT);

        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void deposit() {
        try {
            long accountNo = Long.parseLong(txtAccountNumber.getText().trim());
            double amount = Double.parseDouble(txtAmount.getText().trim());

            String result = bankService.deposit(accountNo, amount);
            
            if (result.contains("successful")) {
                showSuccess(result);
                new Timer(2000, e -> dispose()).start();
            } else {
                showError(result);
            }
        } catch (NumberFormatException e) {
            showError("Invalid input");
        }
    }

    private void withdraw() {
        try {
            long accountNo = Long.parseLong(txtAccountNumber.getText().trim());
            double amount = Double.parseDouble(txtAmount.getText().trim());

            String result = bankService.withdraw(accountNo, amount);
            
            if (result.contains("successful")) {
                showSuccess(result);
                new Timer(2000, e -> dispose()).start();
            } else {
                showError(result);
            }
        } catch (NumberFormatException e) {
            showError("Invalid input");
        }
    }

    private void showError(String message) {
        lblMessage.setForeground(ERROR_COLOR);
        lblMessage.setText(message);
    }

    private void showSuccess(String message) {
        lblMessage.setForeground(new Color(76, 175, 80));
        lblMessage.setText(message);
    }

    private void searchAccount() {
        // Stub method - can be implemented later if needed
    }
}

