package com.newtech.Services.Security.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailClientService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationConfirmationEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Confirmation d'inscription");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>" +
                            "<h2 style='color: #333;'>Bienvenue, " + name + "!</h2>" +
                            "<p>Votre inscription a été effectuée avec succès.</p>" +
                            "<p>Vous pouvez maintenant vous connecter à votre compte en utilisant votre email et mot de passe.</p>" +
                            "<a href='http://votre-site.com/login' style='display: inline-block; margin: 20px 0; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;'>Se connecter</a>" +
                            "<p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>" +
                            "<p>Cordialement,<br>L'équipe de votre site</p>" +
                            "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Registration confirmation email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send registration confirmation email to {}: {}", to, e.getMessage());
        }
    }

    public void sendAccountDeletionEmail(String to, String name) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Confirmation de suppression de compte");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>" +
                            "<h2 style='color: #333;'>Au revoir, " + name + "!</h2>" +
                            "<p>Votre compte a été supprimé avec succès.</p>" +
                            "<p>Nous sommes désolés de vous voir partir. Si vous souhaitez nous rejoindre à nouveau, vous pouvez créer un nouveau compte à tout moment.</p>" +
                            "<p>Si vous avez des commentaires ou suggestions pour améliorer nos services, n'hésitez pas à nous contacter à <a href='mailto:contact@votre-site.com'>contact@votre-site.com</a>.</p>" +
                            "<p>Cordialement,<br>L'équipe de votre site</p>" +
                            "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Account deletion confirmation email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send account deletion email to {}: {}", to, e.getMessage());
        }
    }

    public void sendDeletionConfirmationRequest(String to, String name, String confirmationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Confirmation de suppression de compte");

            String htmlContent =
                    "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 5px;'>" +
                            "<h2 style='color: #333;'>Demande de suppression de compte</h2>" +
                            "<p>Bonjour " + name + ",</p>" +
                            "<p>Nous avons reçu une demande de suppression de votre compte.</p>" +
                            "<p>Si vous souhaitez vraiment supprimer votre compte, veuillez cliquer sur le lien ci-dessous pour confirmer :</p>" +
                            "<a href='" + confirmationLink + "' style='display: inline-block; margin: 20px 0; padding: 10px 20px; background-color: #f44336; color: white; text-decoration: none; border-radius: 5px;'>Confirmer la suppression</a>" +
                            "<p>Ce lien expirera dans 24 heures.</p>" +
                            "<p>Si vous n'avez pas demandé cette suppression, vous pouvez ignorer cet email ou nous contacter immédiatement si vous pensez que votre compte a été compromis.</p>" +
                            "<p>Cordialement,<br>L'équipe de votre site</p>" +
                            "</div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Deletion confirmation request email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send deletion confirmation request to {}: {}", to, e.getMessage());
        }
    }
}
