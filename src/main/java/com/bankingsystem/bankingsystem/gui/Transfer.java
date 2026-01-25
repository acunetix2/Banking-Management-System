package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import java.awt.*;

public class Transfer extends JDialog {

    public Transfer(BankService bankService) {
        setTitle("Transfer Money");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel fromLabel = new JLabel("From Account:");
        JTextField fromField = new JTextField();

        JLabel toLabel = new JLabel("To Account:");
        JTextField toField = new JTextField();

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JButton transferBtn = styleButton(new JButton("Transfer"), new Color(33, 150, 243));
        transferBtn.addActionListener(e -> {
            try {
                long fromAccount = Long.parseLong(fromField.getText().trim());
                long toAccount = Long.parseLong(toField.getText().trim());
                double amount = Double.parseDouble(amountField.getText().trim());

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String result = bankService.transfer(fromAccount, toAccount, amount);
                
                if (result.contains("successful")) {
                    JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton closeBtn = styleButton(new JButton("Close"), new Color(150, 150, 150));
        closeBtn.addActionListener(e -> dispose());

        panel.add(fromLabel);
        panel.add(fromField);
        panel.add(toLabel);
        panel.add(toField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(transferBtn);
        panel.add(closeBtn);

        add(panel);
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
