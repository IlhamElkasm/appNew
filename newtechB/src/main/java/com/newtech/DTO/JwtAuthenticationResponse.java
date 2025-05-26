package com.newtech.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType;
    private Set<String> roles;
    private String nom;

    public JwtAuthenticationResponse(String accessToken, Set<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
        this.tokenType = "Bearer";
        this.nom = ""; // Initialiser avec une chaîne vide au lieu de null
    }
}
