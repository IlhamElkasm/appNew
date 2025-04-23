package com.newtech.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private Set<String> roles;  // Keep as String for frontend compatibility

    public JwtAuthenticationResponse(String accessToken, Set<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }
}
