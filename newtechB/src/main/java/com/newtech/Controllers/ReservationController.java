package com.newtech.Controllers;

import com.newtech.DTO.ReservationDTO;
import com.newtech.Enum.ReservationStatus;
import com.newtech.Model.Client;
import com.newtech.Model.User;
import com.newtech.Repositories.UserRepository;
import com.newtech.Services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        try {
            List<ReservationDTO> reservations = reservationService.getAllReservations();
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all reservations: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        try {
            return reservationService.getReservationById(id)
                    .map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            System.err.println("Error fetching reservation by ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByClientId(@PathVariable Long clientId) {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsByClientId(clientId);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching reservations by client ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByFormationId(@PathVariable Long formationId) {
        try {
            List<ReservationDTO> reservations = reservationService.getReservationsByFormationId(formationId);
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching reservations by formation ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/client/add")
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Long> payload) {
        try {
            Long formationId = payload.get("formationId");
            if (formationId == null) {
                return ResponseEntity.badRequest().body("Formation ID is required");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));

            if (!(user instanceof Client)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only clients can make reservations");
            }

            Client client = (Client) user;
            ReservationDTO createdReservation = reservationService.createReservation(client.getId(), formationId);

            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error creating reservation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating reservation: " + e.getMessage());
        }
    }

    @GetMapping("/admin/count/new")
    public ResponseEntity<Long> countNewReservations() {
        try {
            long count = reservationService.countNewReservations();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error counting new reservations: " + e.getMessage());
            return new ResponseEntity<>(0L, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/validate")
    public ResponseEntity<Map<String, String>> validateReservation(@PathVariable Long id) {
        try {
            System.out.println("Validating reservation with ID: " + id);

            reservationService.validateReservation(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Reservation validated successfully and confirmation email sent");
            response.put("status", "success");

            System.out.println("Reservation " + id + " validated successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error validating reservation " + id + ": " + e.getMessage());
            e.printStackTrace();

            Map<String, String> errorResponse = new HashMap<>();

            if (e.getMessage().contains("not found")) {
                errorResponse.put("message", "Reservation not found");
                errorResponse.put("status", "error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } else {
                errorResponse.put("message", "Error validating reservation: " + e.getMessage());
                errorResponse.put("status", "error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Map<String, String>> cancelMyReservation(@PathVariable Long id) {
        try {
            // Verify that the reservation belongs to the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));

            if (!(user instanceof Client)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Only clients can cancel reservations");
                response.put("status", "error");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            // Check if reservation belongs to this client
            Optional<ReservationDTO> reservationOpt = reservationService.getReservationById(id);
            if (reservationOpt.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Reservation not found");
                response.put("status", "error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Client client = (Client) user;
            ReservationDTO reservation = reservationOpt.get();

            if (!reservation.getClientId().equals(client.getId())) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "You can only cancel your own reservations");
                response.put("status", "error");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            reservationService.cancelReservation(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Reservation cancelled successfully and cancellation email sent");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error cancelling reservation " + id + ": " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error cancelling reservation: " + e.getMessage());
            errorResponse.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Additional endpoint for admin to cancel any reservation
    @PutMapping("/admin/{id}/cancel")
    public ResponseEntity<Map<String, String>> adminCancelReservation(@PathVariable Long id) {
        try {
            reservationService.cancelReservation(id);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Reservation cancelled successfully by admin and cancellation email sent");
            response.put("status", "success");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error in admin cancelling reservation " + id + ": " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            if (e.getMessage().contains("not found")) {
                errorResponse.put("message", "Reservation not found");
                errorResponse.put("status", "error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            } else {
                errorResponse.put("message", "Error cancelling reservation: " + e.getMessage());
                errorResponse.put("status", "error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }
}