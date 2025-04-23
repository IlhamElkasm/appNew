package com.newtech.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientRegistrationRequest extends BaseRegistrationRequest {
    @NotBlank(message = "Phone number cannot be blank")
    private String telephone;
    private String adresse;

}

