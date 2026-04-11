package shako.schoolmanagement.service.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import shako.schoolmanagement.service.inter.MailService;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Override
    public void sendMail(String toMail, String subject, String body) {
        log.info("Sending mail to: {}, subject: '{}'", toMail, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(toMail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        log.info("Mail sent successfully to: {}", toMail);
    }
}
