package com.ithelpdesk.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    private String smtpHost;
    private int smtpPort;
    private String smtpUsername;
    private String smtpPassword;

    private boolean isDummyMode;

    // Dummy mode constructor
    public EmailService() {
        this.isDummyMode = true;
        System.out.println("EmailService initialized in dummy mode.");
    }

    // Real SMTP mode constructor
    public EmailService(String smtpHost, int smtpPort, String smtpUsername, String smtpPassword) {
        this.isDummyMode = false;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        System.out.println("EmailService initialized with SMTP configuration.");
    }

    public void sendEmail(String recipient, String subject, String body) {
        if (isDummyMode) {
            System.out.println("Dummy Email Sent to " + recipient + " with subject: " + subject);
            return;
        }

        // SMTP email sending logic
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", String.valueOf(smtpPort));

            // Authenticate with SMTP server
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            });

            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully to " + recipient + " with subject: " + subject);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
