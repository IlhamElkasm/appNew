package com.newtech.Mapper;

import com.newtech.DTO.ReservationDTO;
import com.newtech.Model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "formation.id", target = "formationId")
    @Mapping(source = "payment.id", target = "paymentId")
    ReservationDTO toDto(Reservation reservation);

    List<ReservationDTO> toDtoList(List<Reservation> reservations);
}
