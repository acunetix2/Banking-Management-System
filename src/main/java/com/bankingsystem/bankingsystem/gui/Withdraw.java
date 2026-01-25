package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import java.awt.*;

public class Withdraw extends JDialog {

    public Withdraw(BankService bankService) {
        setTitle("Withdraw Money");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel accLabel = new JLabel("Account Number:");
        JTextField accField = new JTextField();

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.addActionListener(e -> {
            try {
                long accountNo = Long.parseLong(accField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());

                String result = bankService.withdraw(accountNo, amount);
                JOptionPane.showMessageDialog(this, result, "Withdraw", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        panel.add(accLabel);
        panel.add(accField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(withdrawBtn);
        panel.add(closeBtn);

        add(panel);
    }
}
