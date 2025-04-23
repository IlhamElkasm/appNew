package com.newtech.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newtech.Enum.ReservationStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Client client;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt = LocalDateTime.now();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;
}
