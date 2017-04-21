package org.alcibiade.pandiscovery.db.service;

import org.alcibiade.pandiscovery.db.model.DiscoveryReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * Send an email message report
 */
@Component
@Profile("mail")
public class MessageService {
    private Logger logger = LoggerFactory.getLogger(MessageService.class);

    private String sender;
    private String recipient;
    private JavaMailSender mailSender;

    @Autowired
    public MessageService(JavaMailSender mailSender,
                          @Value("${pan-discovery.reports.recipient}") String recipient,
                          @Value("${pan-discovery.reports.sender}") String sender) {
        this.sender = sender;
        this.mailSender = mailSender;
        this.recipient = recipient;
    }

    public void sendReports(DiscoveryReport report, List<File> reportFiles) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(this.recipient);
            helper.setFrom(sender);
            helper.setSubject("PAN Discovery report for " + report.getDatabaseName());
            helper.setText("Find attached the reports of the scan on "
                    + report.getDatabaseName()
                    + " run on " + report.getReportDateStart());


            for (File reportFile : reportFiles) {
                FileSystemResource fileSystemResource = new FileSystemResource(reportFile);
                helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }

            this.mailSender.send(message);
            logger.info("Reports sent to {}", recipient);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }
}
