package com.bankingsystem.bankingsystem.gui;

import com.bankingsystem.bankingsystem.model.Account;
import com.bankingsystem.bankingsystem.model.User;
import com.bankingsystem.bankingsystem.service.BankService;

import javax.swing.*;
import java.awt.*;

public class CreateAccount extends JDialog {
    private JTextField txtName;
    private JComboBox<String> cmbType;
    private JTextField txtBalance;
    private BankService bankService;

    public CreateAccount(BankService bankService, User user) {
        this.bankService = bankService;

        setTitle("Create New Account");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setModal(true);
        setResizable(true);

        JPanel main = new JPanel(new GridLayout(4, 2, 10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        main.setBackground(new Color(240, 240, 240));

        main.add(new JLabel("Customer Name:"));
        txtName = new JTextField();
        main.add(txtName);

        main.add(new JLabel("Account Type:"));
        cmbType = new JComboBox<>(new String[]{"Savings", "Checking", "Business"});
        main.add(cmbType);

        main.add(new JLabel("Initial Balance:"));
        txtBalance = new JTextField();
        main.add(txtBalance);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(new Color(240, 240, 240));
        
        JButton btnCreate = styleButton(new JButton("Create"), new Color(76, 175, 80));
        btnCreate.addActionListener(e -> createAccount());
        
        JButton btnCancel = styleButton(new JButton("Cancel"), new Color(158, 158, 158));
        btnCancel.addActionListener(e -> dispose());
        
        btnPanel.add(btnCreate);
        btnPanel.add(btnCancel);

        JPanel container = new JPanel(new BorderLayout());
        container.add(main, BorderLayout.CENTER);
        container.add(btnPanel, BorderLayout.SOUTH);

        add(container);
    }

    private void createAccount() {
        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer name is required.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double balance = Double.parseDouble(txtBalance.getText().trim());
            if (balance < 0) {
                JOptionPane.showMessageDialog(this, "Balance cannot be negative.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                return;
            }

            long accNo = bankService.generateAccountNumber();
            Account acc = new Account(accNo, (String) cmbType.getSelectedItem(), name, balance);
            bankService.createAccount(acc);
            
            JOptionPane.showMessageDialog(this, 
                "Account Created Successfully!\n\nAccount Number: " + accNo + "\nName: " + name + 
                "\nType: " + cmbType.getSelectedItem() + "\nBalance: " + balance,
                "Success", JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid balance amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
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
