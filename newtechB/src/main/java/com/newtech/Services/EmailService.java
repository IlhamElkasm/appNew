//package com.newtech.Services;
//
//import com.newtech.Model.Reservation;
//import com.newtech.Model.Client;
//import com.newtech.Model.Formation;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.time.format.DateTimeFormatter;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Value("${spring.mail.username}")
//    private String fromEmail;
//
//    @Value("${app.name:NewTech Formation}")
//    private String appName;
//
//    // Send simple email for reservation confirmation
//    public void sendReservationConfirmationEmail(Reservation reservation) {
//        try {
//            Client client = reservation.getClient();
//            Formation formation = reservation.getFormation();
//
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(client.getEmail());
//            message.setSubject("Confirmation de votre réservation - " + formation.getTitle());
//
//            String emailBody = buildConfirmationEmailBody(client, formation, reservation);
//            message.setText(emailBody);
//
//            mailSender.send(message);
//            System.out.println("Confirmation email sent to: " + client.getEmail());
//
//        } catch (Exception e) {
//            System.err.println("Failed to send confirmation email: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // Send HTML email for better formatting
//    public void sendReservationConfirmationEmailHTML(Reservation reservation) {
//        try {
//            Client client = reservation.getClient();
//            Formation formation = reservation.getFormation();
//
//            MimeMessage mimeMessage = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//
//            helper.setFrom(fromEmail);
//            helper.setTo(client.getEmail());
//            helper.setSubject("✅ Confirmation de votre réservation - " + formation.getTitle());
//
//            String htmlBody = buildConfirmationEmailHTMLBody(client, formation, reservation);
//            helper.setText(htmlBody, true);
//
//            mailSender.send(mimeMessage);
//            System.out.println("HTML confirmation email sent to: " + client.getEmail());
//
//        } catch (MessagingException e) {
//            System.err.println("Failed to send HTML confirmation email: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // Send email when reservation is pending (just created)
//    public void sendReservationPendingEmail(Reservation reservation) {
//        try {
//            Client client = reservation.getClient();
//            Formation formation = reservation.getFormation();
//
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(client.getEmail());
//            message.setSubject("Réservation reçue - " + formation.getTitle());
//
//            String emailBody = buildPendingEmailBody(client, formation, reservation);
//            message.setText(emailBody);
//
//            mailSender.send(message);
//            System.out.println("Pending notification email sent to: " + client.getEmail());
//
//        } catch (Exception e) {
//            System.err.println("Failed to send pending email: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    // Build simple text email body for confirmation
//    private String buildConfirmationEmailBody(Client client, Formation formation, Reservation reservation) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
//
//        return String.format(
//                "Bonjour %s,\n\n" +
//                        "Nous avons le plaisir de vous confirmer que votre réservation pour la formation \"%s\" a été validée!\n\n" +
//                        "Détails de votre réservation:\n" +
//                        "- Formation: %s\n" +
//                        "- Description: %s\n" +
//                        "- Date de réservation: %s\n" +
//                        "- Numéro de réservation: #%d\n" +
//                        "- Statut: CONFIRMÉE ✅\n\n" +
//                        "Vous pouvez maintenant accéder à votre formation en vous connectant à votre espace client.\n\n" +
//                        "Si vous avez des questions, n'hésitez pas à nous contacter.\n\n" +
//                        "Cordialement,\n" +
//                        "L'équipe %s",
//
//                client.getNom(),
//                formation.getTitle(),
//                formation.getTitle(),
//                formation.getDescription() != null ? formation.getDescription() : "Aucune description disponible",
//                reservation.getReservedAt().format(formatter),
//                reservation.getId(),
//                appName
//        );
//    }
//
//    // Build HTML email body for better presentation
//    private String buildConfirmationEmailHTMLBody(Client client, Formation formation, Reservation reservation) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
//
//        return String.format("""
//            <!DOCTYPE html>
//            <html>
//            <head>
//                <meta charset="UTF-8">
//                <style>
//                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
//                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
//                    .header { background-color: #007bff; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }
//                    .content { background-color: #f8f9fa; padding: 30px; border-radius: 0 0 8px 8px; }
//                    .success-badge { background-color: #28a745; color: white; padding: 8px 16px; border-radius: 20px; display: inline-block; margin: 10px 0; }
//                    .details { background-color: white; padding: 20px; border-radius: 8px; margin: 20px 0; border-left: 4px solid #007bff; }
//                    .btn { background-color: #007bff; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; display: inline-block; margin: 20px 0; }
//                    .footer { text-align: center; margin-top: 30px; color: #666; font-size: 14px; }
//                </style>
//            </head>
//            <body>
//                <div class="container">
//                    <div class="header">
//                        <h1>🎉 Réservation Confirmée!</h1>
//                    </div>
//                    <div class="content">
//                        <p>Bonjour <strong>%s</strong>,</p>
//
//                        <p>Nous avons le plaisir de vous confirmer que votre réservation a été validée!</p>
//
//                        <div class="success-badge">✅ CONFIRMÉE</div>
//
//                        <div class="details">
//                            <h3>📚 Détails de votre formation</h3>
//                            <p><strong>Formation:</strong> %s</p>
//                            <p><strong>Description:</strong> %s</p>
//                            <p><strong>Date de réservation:</strong> %s</p>
//                            <p><strong>Numéro de réservation:</strong> #%d</p>
//                        </div>
//
//                        <p>Vous pouvez maintenant accéder à votre formation en vous connectant à votre espace client.</p>
//
//                        <a href="#" class="btn">🚀 Accéder à ma formation</a>
//
//                        <p>Si vous avez des questions, n'hésitez pas à nous contacter.</p>
//                    </div>
//                    <div class="footer">
//                        <p>Cordialement,<br>L'équipe <strong>%s</strong></p>
//                        <p><small>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</small></p>
//                    </div>
//                </div>
//            </body>
//            </html>
//            """,
//
//                client.getNom(),
//                formation.getTitle(),
//                formation.getDescription() != null ? formation.getDescription() : "Aucune description disponible",
//                reservation.getReservedAt().format(formatter),
//                reservation.getId(),
//                appName
//        );
//    }
//
//    // Build pending email body
//    private String buildPendingEmailBody(Client client, Formation formation, Reservation reservation) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
//
//        return String.format(
//                "Bonjour %s,\n\n" +
//                        "Nous avons bien reçu votre demande de réservation pour la formation \"%s\".\n\n" +
//                        "Détails de votre demande:\n" +
//                        "- Formation: %s\n" +
//                        "- Date de demande: %s\n" +
//                        "- Numéro de réservation: #%d\n" +
//                        "- Statut: EN ATTENTE DE VALIDATION ⏳\n\n" +
//                        "Votre demande sera traitée dans les plus brefs délais par notre équipe.\n" +
//                        "Vous recevrez un email de confirmation dès que votre réservation sera validée.\n\n" +
//                        "Merci pour votre confiance.\n\n" +
//                        "Cordialement,\n" +
//                        "L'équipe %s",
//
//                client.getNom(),
//                formation.getTitle(),
//                formation.getTitle(),
//                reservation.getReservedAt().format(formatter),
//                reservation.getId(),
//                appName
//        );
//    }
//
//    // Send cancellation email
//    public void sendReservationCancellationEmail(Reservation reservation) {
//        try {
//            Client client = reservation.getClient();
//            Formation formation = reservation.getFormation();
//
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom(fromEmail);
//            message.setTo(client.getEmail());
//            message.setSubject("Annulation de votre réservation - " + formation.getTitle());
//
//            String emailBody = String.format(
//                    "Bonjour %s,\n\n" +
//                            "Nous vous confirmons l'annulation de votre réservation pour la formation \"%s\".\n\n" +
//                            "Numéro de réservation: #%d\n" +
//                            "Statut: ANNULÉE ❌\n\n" +
//                            "Si cette annulation n'était pas prévue, veuillez nous contacter immédiatement.\n\n" +
//                            "Cordialement,\n" +
//                            "L'équipe %s",
//
//                    client.getNom(),
//                    formation.getTitle(),
//                    reservation.getId(),
//                    appName
//            );
//
//            message.setText(emailBody);
//            mailSender.send(message);
//            System.out.println("Cancellation email sent to: " + client.getEmail());
//
//        } catch (Exception e) {
//            System.err.println("Failed to send cancellation email: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}



package com.newtech.Services;

import com.newtech.Model.Reservation;
import com.newtech.Model.Client;
import com.newtech.Model.Formation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.name:NewTech Formation}")
    private String appName;

    @Value("${app.domain:localhost:4200}")
    private String appDomain;

    @Value("${app.support.email:support@newtech.com}")
    private String supportEmail;

    // Improved HTML email with anti-spam measures
    public void sendReservationConfirmationEmailHTML(Reservation reservation) {
        try {
            Client client = reservation.getClient();
            Formation formation = reservation.getFormation();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Anti-spam headers
            helper.setFrom(fromEmail, appName); // Set display name
            helper.setTo(client.getEmail());
            helper.setReplyTo(supportEmail); // Set proper reply-to

            // Clear, professional subject line
            helper.setSubject("Confirmation de réservation #" + reservation.getId() + " - " + appName);

            String htmlBody = buildImprovedConfirmationEmailHTML(client, formation, reservation);
            helper.setText(htmlBody, true);

            // Add headers to avoid spam
            mimeMessage.setHeader("X-Priority", "3"); // Normal priority
            mimeMessage.setHeader("X-Mailer", appName);
            mimeMessage.setHeader("List-Unsubscribe", "<mailto:" + supportEmail + "?subject=Unsubscribe>");

            mailSender.send(mimeMessage);
            System.out.println("Confirmation email sent successfully to: " + client.getEmail());

        } catch (MessagingException e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // Send pending email with improved anti-spam measures
    public void sendReservationPendingEmail(Reservation reservation) {
        try {
            Client client = reservation.getClient();
            Formation formation = reservation.getFormation();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail, appName);
            helper.setTo(client.getEmail());
            helper.setReplyTo(supportEmail);
            helper.setSubject("Demande de réservation reçue #" + reservation.getId() + " - " + appName);

            String htmlBody = buildPendingEmailHTML(client, formation, reservation);
            helper.setText(htmlBody, true);

            // Anti-spam headers
            mimeMessage.setHeader("X-Priority", "3");
            mimeMessage.setHeader("X-Mailer", appName);
            mimeMessage.setHeader("List-Unsubscribe", "<mailto:" + supportEmail + "?subject=Unsubscribe>");

            mailSender.send(mimeMessage);
            System.out.println("Pending email sent successfully to: " + client.getEmail());

        } catch (MessagingException e) {
            System.err.println("Failed to send pending email: " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // Improved HTML template with better structure
    private String buildImprovedConfirmationEmailHTML(Client client, Formation formation, Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        return String.format("""
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Confirmation de réservation</title>
                <style>
                    body { 
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
                        line-height: 1.6; 
                        color: #333333; 
                        margin: 0; 
                        padding: 0; 
                        background-color: #f4f4f4;
                    }
                    .email-container { 
                        max-width: 600px; 
                        margin: 20px auto; 
                        background-color: #ffffff;
                        border-radius: 8px;
                        overflow: hidden;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    .header { 
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        color: white; 
                        padding: 30px 20px; 
                        text-align: center; 
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 24px;
                        font-weight: 600;
                    }
                    .content { 
                        padding: 30px; 
                    }
                    .success-badge { 
                        background-color: #28a745; 
                        color: white; 
                        padding: 8px 16px; 
                        border-radius: 20px; 
                        display: inline-block; 
                        margin: 15px 0;
                        font-weight: 600;
                    }
                    .details-box { 
                        background-color: #f8f9fa; 
                        padding: 20px; 
                        border-radius: 8px; 
                        margin: 20px 0; 
                        border-left: 4px solid #667eea;
                    }
                    .detail-row {
                        margin: 10px 0;
                        padding: 5px 0;
                        border-bottom: 1px solid #e9ecef;
                    }
                    .detail-row:last-child {
                        border-bottom: none;
                    }
                    .detail-label {
                        font-weight: 600;
                        color: #495057;
                    }
                    .btn { 
                        background-color: #667eea; 
                        color: white; 
                        padding: 12px 24px; 
                        text-decoration: none; 
                        border-radius: 5px; 
                        display: inline-block; 
                        margin: 20px 0;
                        font-weight: 600;
                    }
                    .footer { 
                        background-color: #f8f9fa;
                        text-align: center; 
                        padding: 20px; 
                        color: #6c757d; 
                        font-size: 14px;
                        border-top: 1px solid #dee2e6;
                    }
                    .company-info {
                        margin-top: 15px;
                        font-size: 12px;
                        color: #adb5bd;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <h1>✅ Réservation Confirmée</h1>
                        <p style="margin: 10px 0 0 0; opacity: 0.9;">%s</p>
                    </div>
                    
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        
                        <p>Nous avons le plaisir de vous confirmer que votre réservation a été validée avec succès.</p>
                        
                        <div class="success-badge">✅ CONFIRMÉE</div>
                        
                        <div class="details-box">
                            <h3 style="margin-top: 0; color: #495057;">📚 Détails de votre réservation</h3>
                            
                            <div class="detail-row">
                                <span class="detail-label">Formation :</span><br>
                                <strong>%s</strong>
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Description :</span><br>
                                %s
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Date de réservation :</span><br>
                                %s
                            </div>
                            
                            <div class="detail-row">
                                <span class="detail-label">Numéro de réservation :</span><br>
                                <strong>#%d</strong>
                            </div>
                        </div>
                        
                        <p>Vous pouvez maintenant accéder à votre formation en vous connectant à votre espace client.</p>
                        
                        <a href="http://%s/login" class="btn">🚀 Accéder à mon espace</a>
                        
                        <p>Pour toute question, vous pouvez nous contacter à <a href="mailto:%s">%s</a></p>
                    </div>
                    
                    <div class="footer">
                        <p><strong>Cordialement,</strong><br>L'équipe %s</p>
                        
                        <div class="company-info">
                            <p>Cet email a été envoyé automatiquement depuis notre système de gestion des formations.<br>
                            Si vous avez reçu cet email par erreur, veuillez nous contacter.</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
                appName,
                client.getNom(),
                formation.getTitle(),
                formation.getDescription() != null ? formation.getDescription() : "Aucune description disponible",
                reservation.getReservedAt().format(formatter),
                reservation.getId(),
                appDomain,
                supportEmail,
                supportEmail,
                appName
        );
    }

    // Improved pending email HTML
    private String buildPendingEmailHTML(Client client, Formation formation, Reservation reservation) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

        return String.format("""
            <!DOCTYPE html>
            <html lang="fr">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Demande de réservation reçue</title>
                <style>
                    body { 
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
                        line-height: 1.6; 
                        color: #333333; 
                        margin: 0; 
                        padding: 0; 
                        background-color: #f4f4f4;
                    }
                    .email-container { 
                        max-width: 600px; 
                        margin: 20px auto; 
                        background-color: #ffffff;
                        border-radius: 8px;
                        overflow: hidden;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    .header { 
                        background: linear-gradient(135deg, #ffc107 0%%, #ff8c00 100%%);
                        color: white; 
                        padding: 30px 20px; 
                        text-align: center; 
                    }
                    .content { padding: 30px; }
                    .pending-badge { 
                        background-color: #ffc107; 
                        color: #212529; 
                        padding: 8px 16px; 
                        border-radius: 20px; 
                        display: inline-block; 
                        margin: 15px 0;
                        font-weight: 600;
                    }
                    .details-box { 
                        background-color: #f8f9fa; 
                        padding: 20px; 
                        border-radius: 8px; 
                        margin: 20px 0; 
                        border-left: 4px solid #ffc107;
                    }
                    .footer { 
                        background-color: #f8f9fa;
                        text-align: center; 
                        padding: 20px; 
                        color: #6c757d; 
                        font-size: 14px;
                        border-top: 1px solid #dee2e6;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <h1>⏳ Demande Reçue</h1>
                        <p style="margin: 10px 0 0 0; opacity: 0.9;">%s</p>
                    </div>
                    
                    <div class="content">
                        <p>Bonjour <strong>%s</strong>,</p>
                        
                        <p>Nous avons bien reçu votre demande de réservation. Elle sera traitée dans les plus brefs délais.</p>
                        
                        <div class="pending-badge">⏳ EN ATTENTE DE VALIDATION</div>
                        
                        <div class="details-box">
                            <h3 style="margin-top: 0;">📚 Détails de votre demande</h3>
                            <p><strong>Formation :</strong> %s</p>
                            <p><strong>Date de demande :</strong> %s</p>
                            <p><strong>Numéro de réservation :</strong> #%d</p>
                        </div>
                        
                        <p>Vous recevrez un email de confirmation dès que votre réservation sera validée par notre équipe.</p>
                        
                        <p>Pour toute question, contactez-nous à <a href="mailto:%s">%s</a></p>
                    </div>
                    
                    <div class="footer">
                        <p><strong>Merci pour votre confiance,</strong><br>L'équipe %s</p>
                    </div>
                </div>
            </body>
            </html>
            """,
                appName,
                client.getNom(),
                formation.getTitle(),
                reservation.getReservedAt().format(formatter),
                reservation.getId(),
                supportEmail,
                supportEmail,
                appName
        );
    }

    // Cancellation email
    public void sendReservationCancellationEmail(Reservation reservation) {
        try {
            Client client = reservation.getClient();
            Formation formation = reservation.getFormation();

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail, appName);
            helper.setTo(client.getEmail());
            helper.setReplyTo(supportEmail);
            helper.setSubject("Annulation de réservation #" + reservation.getId() + " - " + appName);

            String htmlBody = String.format("""
                <!DOCTYPE html>
                <html lang="fr">
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                        .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #dc3545; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background-color: #f8f9fa; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>❌ Réservation Annulée</h1>
                        </div>
                        <div class="content">
                            <p>Bonjour <strong>%s</strong>,</p>
                            <p>Nous vous confirmons l'annulation de votre réservation :</p>
                            <p><strong>Formation :</strong> %s</p>
                            <p><strong>Numéro :</strong> #%d</p>
                            <p>Si cette annulation n'était pas prévue, contactez-nous à <a href="mailto:%s">%s</a></p>
                            <p>Cordialement,<br>L'équipe %s</p>
                        </div>
                    </div>
                </body>
                </html>
                """,
                    client.getNom(),
                    formation.getTitle(),
                    reservation.getId(),
                    supportEmail,
                    supportEmail,
                    appName
            );

            helper.setText(htmlBody, true);

            mimeMessage.setHeader("X-Priority", "3");
            mimeMessage.setHeader("X-Mailer", appName);

            mailSender.send(mimeMessage);
            System.out.println("Cancellation email sent successfully to: " + client.getEmail());

        } catch (MessagingException e) {
            System.err.println("Failed to send cancellation email: " + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}