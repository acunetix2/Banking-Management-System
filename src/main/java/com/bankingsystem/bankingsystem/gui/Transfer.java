package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Transfer extends JDialog {

    private JTextField txtFromAccount;
    private JTextField txtToAccount;
    private JTextField txtAmount;
    private BankService bankService;

    private final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 11);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);
    private final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color GRAY_BG = new Color(245, 245, 945);

    public Transfer(BankService bankService) {
        this.bankService = bankService;

        setTitle("Transfer Money");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(GRAY_BG);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Transfer Money");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel subtitleLabel = new JLabel("Transfer funds between accounts");
        subtitleLabel.setFont(LABEL_FONT);
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(25));

        JPanel formCard = createFormCard();
        mainPanel.add(formCard);

        add(mainPanel);
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel fromLabel = new JLabel("From Account Number");
        fromLabel.setFont(LABEL_FONT);
        fromLabel.setForeground(new Color(80, 80, 80));
        fromLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(fromLabel);
        card.add(Box.createVerticalStrut(8));

        txtFromAccount = new JTextField();
        txtFromAccount.setFont(FIELD_FONT);
        txtFromAccount.setBackground(GRAY_BG);
        txtFromAccount.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        txtFromAccount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtFromAccount);
        card.add(Box.createVerticalStrut(18));

        JLabel toLabel = new JLabel("To Account Number");
        toLabel.setFont(LABEL_FONT);
        toLabel.setForeground(new Color(80, 80, 80));
        toLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(toLabel);
        card.add(Box.createVerticalStrut(8));

        txtToAccount = new JTextField();
        txtToAccount.setFont(FIELD_FONT);
        txtToAccount.setBackground(GRAY_BG);
        txtToAccount.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        txtToAccount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtToAccount);
        card.add(Box.createVerticalStrut(18));

        JLabel amountLabel = new JLabel("Transfer Amount");
        amountLabel.setFont(LABEL_FONT);
        amountLabel.setForeground(new Color(80, 80, 80));
        amountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(amountLabel);
        card.add(Box.createVerticalStrut(8));

        txtAmount = new JTextField();
        txtAmount.setFont(FIELD_FONT);
        txtAmount.setBackground(GRAY_BG);
        txtAmount.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        txtAmount.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(txtAmount);
        card.add(Box.createVerticalStrut(25));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnCancel = createButton("Cancel", new Color(158, 158, 158));
        btnCancel.addActionListener(e -> dispose());

        JButton btnTransfer = createButton("Transfer", SUCCESS_COLOR);
        btnTransfer.addActionListener(e -> transfer());

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnTransfer);
        card.add(buttonPanel);

        return card;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 40));
        return btn;
    }

    private void transfer() {
        String fromStr = txtFromAccount.getText().trim();
        String toStr = txtToAccount.getText().trim();
        String amountStr = txtAmount.getText().trim();

        if (fromStr.isEmpty() || toStr.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields.", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            long fromAccount = Long.parseLong(fromStr);
            long toAccount = Long.parseLong(toStr);
            double amount = Double.parseDouble(amountStr);

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Amount must be greater than zero.", 
                    "Invalid Amount", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            String result = bankService.transfer(fromAccount, toAccount, amount);
            
            if (result.contains("successful")) {
                JOptionPane.showMessageDialog(this, 
                    result, 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    result, 
                    "Transfer Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Invalid input. Check account numbers and amount.", 
                "Input Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
