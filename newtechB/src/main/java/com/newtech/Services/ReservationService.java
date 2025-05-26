//package com.newtech.Services;
//
//import com.newtech.DTO.ReservationDTO;
//import com.newtech.Enum.ReservationStatus;
//import com.newtech.Model.Client;
//import com.newtech.Model.Formation;
//import com.newtech.Model.Reservation;
//
//import com.newtech.Repositories.ClientRepository;
//import com.newtech.Repositories.FormationRepository;
//import com.newtech.Repositories.ReservationRepository;
//import com.newtech.Mapper.ReservationMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class ReservationService {
//
//    @Autowired
//    private ReservationRepository reservationRepository;
//
//    @Autowired
//    private ClientRepository clientRepository;
//
//    @Autowired
//    private FormationRepository formationRepository;
//    @Autowired
//    private ReservationMapper reservationMapper;
//
//    public List<ReservationDTO> getAllReservations() {
//        List<Reservation> reservations = reservationRepository.findAll();
//        return reservationMapper.toDtoList(reservations);
//    }
//
//    public Optional<ReservationDTO> getReservationById(Long id) {
//        Optional<Reservation> reservation = reservationRepository.findById(id);
//        return reservation.map(reservationMapper::toDto);
//    }
//
//    public List<ReservationDTO> getReservationsByClientId(Long clientId) {
//        List<Reservation> reservations = reservationRepository.findByClientId(clientId);
//        return reservationMapper.toDtoList(reservations);
//    }
//
//    public List<ReservationDTO> getReservationsByFormationId(Long formationId) {
//        List<Reservation> reservations = reservationRepository.findByFormationId(formationId);
//        return reservationMapper.toDtoList(reservations);
//    }
//
//    @Transactional
//    public ReservationDTO createReservation(Long clientId, Long formationId) throws Exception {
//        Client client = clientRepository.findById(clientId)
//                .orElseThrow(() -> new Exception("Client not found with id: " + clientId));
//
//        Formation formation = formationRepository.findById(formationId)
//                .orElseThrow(() -> new Exception("Formation not found with id: " + formationId));
//
//        // Check if there are available slots
//        List<Reservation> existingReservations = reservationRepository.findByFormationId(formationId);
//
//        Reservation reservation = new Reservation();
//        reservation.setClient(client);
//        reservation.setFormation(formation);
//        reservation.setReservedAt(LocalDateTime.now());
//        reservation.setStatus(ReservationStatus.PENDING);
//
//        Reservation savedReservation = reservationRepository.save(reservation);
//
//        return reservationMapper.toDto(savedReservation);
//    }
//
//
//
//    public long countNewReservations() {
//        return reservationRepository.countByStatus(ReservationStatus.PENDING);
//    }
//
//}

package com.newtech.Services;

import com.newtech.DTO.ReservationDTO;
import com.newtech.Enum.ReservationStatus;
import com.newtech.Model.Client;
import com.newtech.Model.Formation;
import com.newtech.Model.Reservation;
import com.newtech.Repositories.ClientRepository;
import com.newtech.Repositories.FormationRepository;
import com.newtech.Repositories.ReservationRepository;
import com.newtech.Mapper.ReservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailService emailService; // Add EmailService

    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservationMapper.toDtoList(reservations);
    }

    public Optional<ReservationDTO> getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        return reservation.map(reservationMapper::toDto);
    }

    public List<ReservationDTO> getReservationsByClientId(Long clientId) {
        List<Reservation> reservations = reservationRepository.findByClientId(clientId);
        return reservationMapper.toDtoList(reservations);
    }

    public List<ReservationDTO> getReservationsByFormationId(Long formationId) {
        List<Reservation> reservations = reservationRepository.findByFormationId(formationId);
        return reservationMapper.toDtoList(reservations);
    }

    @Transactional
    public ReservationDTO createReservation(Long clientId, Long formationId) throws Exception {
        try {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new Exception("Client not found with id: " + clientId));

            Formation formation = formationRepository.findById(formationId)
                    .orElseThrow(() -> new Exception("Formation not found with id: " + formationId));

            Reservation reservation = new Reservation();
            reservation.setClient(client);
            reservation.setFormation(formation);
            reservation.setReservedAt(LocalDateTime.now());
            reservation.setStatus(ReservationStatus.PENDING);

            Reservation savedReservation = reservationRepository.save(reservation);

            // Try-catch for notification to prevent it from failing the whole operation
            try {
                notificationService.createReservationNotificationForAdmin(savedReservation);
            } catch (Exception e) {
                System.err.println("Failed to create notification: " + e.getMessage());
            }

            // Send pending email to client
            try {
                emailService.sendReservationPendingEmail(savedReservation);
            } catch (Exception e) {
                System.err.println("Failed to send pending email: " + e.getMessage());
            }

            return reservationMapper.toDto(savedReservation);
        } catch (Exception e) {
            System.err.println("Error in createReservation: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public long countNewReservations() {
        return reservationRepository.countByStatus(ReservationStatus.PENDING);
    }

    @Transactional
    public void validateReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found"));

        reservation.setStatus(ReservationStatus.CONFIRMED);  // Update status to CONFIRMED
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Send confirmation email to client
        try {
            emailService.sendReservationConfirmationEmailHTML(updatedReservation);
        } catch (Exception e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
            // Don't throw the exception to avoid failing the validation process
        }
    }

    @Transactional
    public void cancelReservation(Long reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new Exception("Reservation not found"));

        reservation.setStatus(ReservationStatus.CANCELLED);  // Update status to CANCELLED
        Reservation updatedReservation = reservationRepository.save(reservation);

        // Send cancellation email to client
        try {
            emailService.sendReservationCancellationEmail(updatedReservation);
        } catch (Exception e) {
            System.err.println("Failed to send cancellation email: " + e.getMessage());
            // Don't throw the exception to avoid failing the cancellation process
        }
    }
}