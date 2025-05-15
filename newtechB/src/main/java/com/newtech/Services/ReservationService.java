package com.newtech.Services;

import com.newtech.DTO.ReservationDTO;
import com.newtech.Enum.NotificationType;
import com.newtech.Enum.ReservationStatus;
import com.newtech.Model.Client;
import com.newtech.Model.Formation;
import com.newtech.Model.Reservation;
import com.newtech.Model.User;
import com.newtech.Repositories.ClientRepository;
import com.newtech.Repositories.FormationRepository;
import com.newtech.Repositories.ReservationRepository;
import com.newtech.Mapper.ReservationMapper; // إضافة ال Mapper
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new Exception("Client not found with id: " + clientId));

        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new Exception("Formation not found with id: " + formationId));

        // Check if there are available slots
        List<Reservation> existingReservations = reservationRepository.findByFormationId(formationId);

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setFormation(formation);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toDto(savedReservation);
    }



    public long countNewReservations() {
        return reservationRepository.countByStatus(ReservationStatus.PENDING);
    }

}
