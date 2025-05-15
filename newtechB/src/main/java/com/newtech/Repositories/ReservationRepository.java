package com.newtech.Repositories;

import com.newtech.Enum.ReservationStatus;
import com.newtech.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByClientId(Long clientId);
    List<Reservation> findByFormationId(Long formationId);
    List<Reservation> findByStatus(ReservationStatus status);

    long countByStatus(ReservationStatus status);

}
