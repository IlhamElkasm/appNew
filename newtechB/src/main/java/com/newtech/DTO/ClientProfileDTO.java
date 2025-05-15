package com.newtech.DTO;

import lombok.Data;

@Data
public class ClientProfileDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
}
