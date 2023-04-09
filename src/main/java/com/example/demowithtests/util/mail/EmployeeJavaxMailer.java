package com.example.demowithtests.util.mail;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.util.exception.EmailSendingException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

//@Component
public final class EmployeeJavaxMailer implements Mailer {
    private final Properties props;
    private final String host = "smtp.gmail.com";
    private final String username = "hilleljavaee2023@gmail.com";
    private final String password = "ngmyysyfqlqipgap";
    private final int port = 587;

    public EmployeeJavaxMailer() {
        this.props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
    }

    @Override
    public void send(Employee e, String subject, String text) {
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(e.getEmail()));
            message.setSubject(subject + "!");
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException exception) {
            throw new EmailSendingException("Sending mail from " + username + " to " + e.getEmail() + " failed.");
        }
    }
}
