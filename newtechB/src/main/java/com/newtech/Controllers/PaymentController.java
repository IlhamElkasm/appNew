package com.newtech.Controllers;


import com.newtech.Enum.PaymentStatus;
import com.newtech.Model.Payment;
import com.newtech.Services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id)
                .map(payment -> new ResponseEntity<>(payment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<Payment> getPaymentByReservationId(@PathVariable Long reservationId) {
        return paymentService.getPaymentByReservationId(reservationId)
                .map(payment -> new ResponseEntity<>(payment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/process")
    public ResponseEntity<Payment> processPayment(@RequestBody Map<String, Object> payload) {
        try {
            Long reservationId = Long.valueOf(payload.get("reservationId").toString());
            Double amount = Double.valueOf(payload.get("amount").toString());

            Payment processedPayment = paymentService.processPayment(reservationId, amount);
            return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        try {
            PaymentStatus status = PaymentStatus.valueOf(payload.get("status"));
            Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

