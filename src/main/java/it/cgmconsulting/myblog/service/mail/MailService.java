package it.cgmconsulting.myblog.service.mail;

import it.cgmconsulting.myblog.entity.User;
import it.cgmconsulting.myblog.service.mail.Mail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    @Value("${app.mail.sender") //recuperato dalle variabili d'ambiente
    private String from;

    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(Mail mail) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);
            mimeMessageHelper.setFrom(mail.getMailFrom());
            mimeMessageHelper.setTo(mail.getMailTo());
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setText(mail.getMailContent());

            javaMailSender.send(mimeMessage);
            log.info(mail.toString());

        } catch (MessagingException e) {
            log.error("ERROR SENDING EMAIL:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public Mail createMail(User u, String subject, String object) { // String s = stringa di comodo da agganciare all'object

        Mail m = new Mail();
        m.setMailFrom(from);
        m.setMailTo(u.getEmail());
        m.setMailSubject(subject);
        m.setMailContent(object);
        return m;
    }
}
