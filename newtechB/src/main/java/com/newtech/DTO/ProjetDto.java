package com.newtech.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjetDto {
    private Long id;
    private String titre;
    private String description;

    private String imageUrl;

}
