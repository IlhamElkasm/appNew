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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations/client")
@CrossOrigin(origins = "*")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservation -> new ResponseEntity<>(reservation, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByClientId(@PathVariable Long clientId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByClientId(clientId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByFormationId(@PathVariable Long formationId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByFormationId(formationId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody Map<String, Long> payload) {
        try {
            Long formationId = payload.get("formationId");

            // Get the currently authenticated user from token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName(); // email from JWT

            // Fetch the user by email
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found"));

            // Check if user is a client
            if (!(user instanceof Client)) {
                throw new Exception("User is not a client");
            }

            Client client = (Client) user;

            // Call the service with clientId
            ReservationDTO createdReservation = reservationService.createReservation(client.getId(), formationId);
            return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/admin/count/new")
    public ResponseEntity<Long> countNewReservations() {
        long count = reservationService.countNewReservations();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }


}
