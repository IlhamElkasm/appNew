//package com.newtech.Mapper;
//
//import com.newtech.DTO.ReservationDTO;
//import com.newtech.Model.Reservation;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface ReservationMapper {
//
//    @Mapping(source = "client.id", target = "clientId")
//    @Mapping(source = "formation.id", target = "formationId")
//    @Mapping(source = "payment.id", target = "paymentId")
//    ReservationDTO toDto(Reservation reservation);
//
//    List<ReservationDTO> toDtoList(List<Reservation> reservations);
//}


package com.newtech.Mapper;

import com.newtech.DTO.ReservationDTO;
import com.newtech.Model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.nom", target = "clientName") // Client has nom field
    @Mapping(source = "client.email", target = "clientEmail") // Client inherits email from User
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "formation.title", target = "formationTitle") // Correction: formationTitle au lieu de formationtitle
    @Mapping(source = "formation.description", target = "formationDescription") // Formation has description field
    @Mapping(source = "payment.id", target = "paymentId")
    ReservationDTO toDto(Reservation reservation);

    List<ReservationDTO> toDtoList(List<Reservation> reservations);
}
