package com.newtech.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clients")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Client extends User {

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    // Additional client-specific fields
    private String telephone;
    private String adresse;

    // Add the missing createdAt field
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Pre-persist hook to automatically set createdAt when a client is created
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
