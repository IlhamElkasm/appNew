package com.newtech.Mapper;
import com.newtech.DTO.FormationDto;
import com.newtech.Model.Formation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FormationMapper {
    FormationDto toDto(Formation formation);
    Formation toEntity(FormationDto dto);

    List<FormationDto> toDtoList(List<Formation> formations);
    List<Formation> toEntityList(List<FormationDto> dtos);
}
