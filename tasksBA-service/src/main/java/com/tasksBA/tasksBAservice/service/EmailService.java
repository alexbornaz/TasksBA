package com.tasksBA.tasksBAservice.service;

import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.util.HtmlTemplateReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    private final HtmlTemplateReader templateReader;

    public EmailService(JavaMailSender mailSender, HtmlTemplateReader templateReader) {
        this.mailSender = mailSender;
        this.templateReader = templateReader;
    }

    @Async
    public void sendRegistrationMail(String to, String name) {
        try {
            MimeMailMessage message = new MimeMailMessage(mailSender.createMimeMessage());
            MimeMessageHelper helper = new MimeMessageHelper(message.getMimeMessage(),
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            String mailContent = templateReader.readHtmlTemplateFrom("templates/registrationTemplate.html");

            mailContent = mailContent.replace("${username}", name);

            helper.setFrom("eosit.alexbornaz@gmail.com", "TO-DO-APP");
            helper.setTo(to);
            helper.setSubject("TO-DO-APP Registration");
            helper.setText(mailContent, true);

            mailSender.send(message.getMimeMessage());

        } catch (Exception e) {
            log.error("Failed attempt to send email to {}", to, e);
        }
    }

    @Async
    public void sendTaskDetailsEmail(String to, Task task, String title) {
        try {
            MimeMailMessage message = new MimeMailMessage(mailSender.createMimeMessage());
            MimeMessageHelper helper = new MimeMessageHelper(message.getMimeMessage(),
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            String mailContent = templateReader.readHtmlTemplateFrom("templates/taskAssign.html");

            mailContent = mailContent.replace("${title}", title);
            mailContent = mailContent.replace("${subject}", task.getSubject());
            mailContent = mailContent.replace("${dueDate}", task.getDueDate().toString());
            mailContent = mailContent.replace("${status}", task.getStatus().toString());
            mailContent = mailContent.replace("${assignedTo}", task.getAssignedTo().getUsername());

            helper.setFrom("eosit.alexbornaz@gmail.com", "TO-DO-APP");
            helper.setTo(to);
            helper.setSubject("TO-DO-APP Task Assignation");
            helper.setText(mailContent, true);

            mailSender.send(message.getMimeMessage());

        } catch (Exception e) {
            log.error("Failed attempt to send assignation email to {}", to, e);
        }
    }
}
