package com.bankingsystem.bankingsystem;

import com.bankingsystem.bankingsystem.gui.Login;
import com.bankingsystem.bankingsystem.service.BankService;
import com.bankingsystem.bankingsystem.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;

@SpringBootApplication
public class BankingsystemApplication {

    public static void main(String[] args) {
        // Force GUI mode - disable headless
        System.setProperty("java.awt.headless", "false");
        
        SpringApplication app = new SpringApplication(BankingsystemApplication.class);
        app.setWebApplicationType(org.springframework.boot.WebApplicationType.NONE);
        ConfigurableApplicationContext context = app.run(args);
        
        BankService bankService = context.getBean(BankService.class);
        UserService userService = context.getBean(UserService.class);

        SwingUtilities.invokeLater(() -> {
            try {
                Login loginGUI = new Login(userService, context, bankService);
                loginGUI.setVisible(true);
                System.out.println("\nBanking System GUI launched successfully!");
            } catch (Exception e) {
                System.err.println("Failed to initialize GUI: " + e.getMessage());
                e.printStackTrace();
                context.close();
                System.exit(1);
            }
        });
    }
}

