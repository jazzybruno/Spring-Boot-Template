package rw.ac.rca.spring_boot_template.services.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import rw.ac.rca.spring_boot_template.models.User;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final ITemplateEngine templateEngine;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, ITemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEmail(User user) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("user", user.getUsername());

        String htmlContent = templateEngine.process("emails/welcome-email.html", context);

        helper.setTo(user.getEmail());
        helper.setSubject("Welcome to Our Application");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendVerificationEmail(User user, String verificationLink) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("user", user.getUsername());
        context.setVariable("verificationLink", verificationLink);

        String htmlContent = templateEngine.process("emails/verify-account.html", context);

        helper.setTo(user.getEmail());
        helper.setSubject("Account Verification");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendPasswordResetEmail(User user, String resetPasswordLink) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        Context context = new Context();
        context.setVariable("user", user.getUsername());
        context.setVariable("resetPasswordLink", resetPasswordLink);

        String htmlContent = templateEngine.process("emails/reset-password.html", context);

        helper.setTo(user.getEmail());
        helper.setSubject("Password Reset Request");
        helper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}
