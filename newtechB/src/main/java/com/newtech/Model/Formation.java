package com.newtech.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "formations")
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(nullable = false)
    private Double price;


    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations;

}
