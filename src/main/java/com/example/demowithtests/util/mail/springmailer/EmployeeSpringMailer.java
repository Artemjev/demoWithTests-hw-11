package com.example.demowithtests.util.mail.springmailer;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.util.mail.Mailer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@AllArgsConstructor
public final class EmployeeSpringMailer implements Mailer {

    private final JavaMailSender mailSender;

    @Override public void send(Employee employee, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(employee.getEmail());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
        log.info("Message sent to employee ({}) successfully", employee);
    }
}
