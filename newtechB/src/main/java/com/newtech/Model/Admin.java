package com.newtech.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "admin")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Admin extends User {
    // Admin-specific fields if needed
}
