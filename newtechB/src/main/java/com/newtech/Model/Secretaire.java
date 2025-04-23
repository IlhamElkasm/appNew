package com.newtech.Model;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("SECRETARY")
public class Secretaire extends User {
    // Secretary-specific fields if needed
}