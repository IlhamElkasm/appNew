package com.newtech.Services;

import com.newtech.Enum.PaymentStatus;
import com.newtech.Enum.ReservationStatus;
import com.newtech.Model.Payment;
import com.newtech.Model.Reservation;
import com.newtech.Repositories.PaymentRepository;
import com.newtech.Repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> getPaymentByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }

    @Transactional
    public Payment processPayment(Long reservationId, Double amount) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found with id: " + reservationId));

        // Check if payment already exists
        Optional<Payment> existingPayment = paymentRepository.findByReservationId(reservationId);
        if (existingPayment.isPresent()) {
            throw new Exception("Payment already exists for this reservation");
        }

        // Create new payment
        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(UUID.randomUUID().toString());

        Payment savedPayment = paymentRepository.save(payment);

        // Update reservation status
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        // Notify secretaries about payment
//        notificationService.notifySecretariesAboutPayment(savedPayment);

        return savedPayment;
    }

    @Transactional
    public Payment updatePaymentStatus(Long id, PaymentStatus status) throws Exception {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new Exception("Payment not found with id: " + id));

        payment.setStatus(status);
        return paymentRepository.save(payment);
    }
}
