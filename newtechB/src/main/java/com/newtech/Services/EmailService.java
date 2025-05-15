//package com.newtech.Services;
//
//import com.newtech.Model.Formation;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.time.format.DateTimeFormatter;
//
//@Service
//public class EmailService {
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendFormationConfirmationEmail(String to, String clientName, Formation formation) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(to);
//            helper.setSubject("Formation Reservation Confirmation");
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            String startDate = formation.getStartDate().format(formatter);
//            String endDate = formation.getEndDate().format(formatter);
//
//            String content =
//                    "<h1>Formation Reservation Confirmation</h1>" +
//                            "<p>Dear " + clientName + ",</p>" +
//                            "<p>Your reservation for the following formation has been confirmed:</p>" +
//                            "<h2>" + formation.getTitle() + "</h2>" +
//                            "<p><strong>Start Date:</strong> " + startDate + "</p>" +
//                            "<p><strong>End Date:</strong> " + endDate + "</p>" +
//                            "<p>Please arrive 15 minutes before the start time.</p>" +
//                            "<p>Thank you for your reservation!</p>";
//
//            helper.setText(content, true);
//
//            mailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
