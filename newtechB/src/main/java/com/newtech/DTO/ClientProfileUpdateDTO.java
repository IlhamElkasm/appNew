package com.newtech.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientProfileUpdateDTO {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String nom;

    @NotBlank(message = "Phone number cannot be blank")
    private String telephone;

    private String adresse;
}
