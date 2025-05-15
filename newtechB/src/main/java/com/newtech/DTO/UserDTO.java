package com.newtech.DTO;

// UserDTO

import com.newtech.Enum.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String nom;
    private String email;
    private UserRole role;
    private Set<UserRole> roles;
    private boolean active;
    private String telephone;
    private String adresse;
}
