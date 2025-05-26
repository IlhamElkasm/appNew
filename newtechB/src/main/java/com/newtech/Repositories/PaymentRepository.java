//package com.newtech.Repositories;
//
//import com.newtech.Enum.PaymentStatus;
//import com.newtech.Model.Payment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    Optional<Payment> findByReservationId(Long reservationId);
//    List<Payment> findByStatus(PaymentStatus status);
//}
