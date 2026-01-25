package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import java.awt.*;

public class AccountOperations extends JDialog {
    private JTextField txtAccNo;
    private JTextField txtAmount;
    private JLabel lblMsg;
    private BankService bankService;

    public AccountOperations(BankService bankService) {
        this.bankService = bankService;

        setTitle("Deposit or Withdraw");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(true);

        JPanel main = new JPanel(new GridLayout(3, 2, 10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        main.setBackground(new Color(240, 240, 240));

        main.add(new JLabel("Account Number:"));
        txtAccNo = new JTextField();
        main.add(txtAccNo);

        main.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        main.add(txtAmount);

        main.add(new JLabel("Status:"));
        lblMsg = new JLabel("");
        lblMsg.setForeground(new Color(244, 67, 54));
        main.add(lblMsg);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnPanel.setBackground(new Color(240, 240, 240));

        JButton deposit = styleButton(new JButton("Deposit"), new Color(76, 175, 80));
        deposit.addActionListener(e -> operation(true));

        JButton withdraw = styleButton(new JButton("Withdraw"), new Color(244, 67, 54));
        withdraw.addActionListener(e -> operation(false));

        JButton cancel = styleButton(new JButton("Cancel"), new Color(158, 158, 158));
        cancel.addActionListener(e -> dispose());

        btnPanel.add(deposit);
        btnPanel.add(withdraw);
        btnPanel.add(cancel);

        JPanel container = new JPanel(new BorderLayout());
        container.add(main, BorderLayout.CENTER);
        container.add(btnPanel, BorderLayout.SOUTH);

        add(container);
    }

    private void operation(boolean isDeposit) {
        try {
            long accNo = Long.parseLong(txtAccNo.getText().trim());
            double amt = Double.parseDouble(txtAmount.getText().trim());
            String result = isDeposit ? bankService.deposit(accNo, amt) : bankService.withdraw(accNo, amt);
            if (result.contains("successful")) {
                lblMsg.setForeground(new Color(76, 175, 80));
                lblMsg.setText("Success " + result);
                new Timer(2000, e -> dispose()).start();
            } else {
                lblMsg.setForeground(new Color(244, 67, 54));
                lblMsg.setText("Invalid " + result);
            }
        } catch (NumberFormatException e) {
            lblMsg.setForeground(new Color(244, 67, 54));
            lblMsg.setText("Invalid input");
        }
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
