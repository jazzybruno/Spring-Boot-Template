package rw.ac.rca.spring_boot_template.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import rw.ac.rca.spring_boot_template.exceptions.BadRequestAlertException;
import rw.ac.rca.spring_boot_template.mailHandler.Mail;
import rw.ac.rca.spring_boot_template.models.User;

import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String appEmail;

    @Value("${app_name}")
    private String appName;

    @Value("${client.host}")
    private String clientHost;
    @Value("${front.host}")
    private String frontHost;

    @Autowired
    public MailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendResetPasswordMail(User user) {
        Mail mail = new Mail(
                appName,
                "Welcome to " + appName + ", You requested to reset your password verify its you to continue",
                user.getUsername() ,
                user.getEmail() ,
                user.getActivationCode(),
                "reset-password"
        );
        sendMail(mail);
    }

    public void sendAccountVerificationEmail(User user) {
        String link =frontHost+"auth/verify-email?email=" + user.getEmail() + "&code=" + user.getActivationCode();
        System.out.println("The link data "+ link);
        Mail mail = new Mail(
                appName,
                "Welcome to "+appName+", Verify your email to continue",
                user.getUsername(), user.getEmail(), link, "verify-email");
        System.out.println("Sending email to "+user.getEmail());
        sendMail(mail);
    }

    public void sendInvitationEmail(User user) {
        String link = frontHost+"auth/accept-invitation?email=" + user.getEmail() + "&token=" + user.getExpirationToken();
        String changeProfile="Invitation to join "+appName;
        Mail mail = new Mail(
                appName,
                changeProfile,
                user.getUsername(),
                user.getEmail(),
                link,
                "invitation");
        sendMail(mail);
    }

    public void sendWelcomeEmail(User user) {
        String link = frontHost;
        Mail mail = new Mail(
                appName,
                "Welcome to"  +appName+", Your account was created",
                user.getUsername(),
                user.getEmail(),
                link,
                "welcome-email"
        );

        sendMail(mail);
    }

    @Async
    public void sendMail(Mail mail) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("app_name",mail.getAppName());
            context.setVariable("link", mail.getData());
            context.setVariable("name", mail.getFullNames());
            context.setVariable("otherData", mail.getOtherData());
            context.setVariable("subject",mail.getSubject());

            System.out.println("IN the mail context");

            String html = templateEngine.process(mail.getTemplate(), context);
            System.out.println("IN the mail context after eraht3");
            helper.setTo(mail.getToEmail());
            System.out.println("IN the mail context after 3");
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            System.out.println("IN the mail context jibeiuer");
            helper.setFrom(appEmail);
            mailSender.send(message);
            System.out.println("The link data in the codes "+ mail.getData());
        } catch (MessagingException exception) {
            exception.printStackTrace();
            throw new BadRequestAlertException("Failed To Send An Email : z"+  exception.getMessage());
        }
    }

}