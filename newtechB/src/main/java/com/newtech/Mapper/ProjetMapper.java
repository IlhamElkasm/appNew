package com.newtech.Mapper;


import com.newtech.DTO.ProjetDto;
import com.newtech.Model.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjetMapper {
    ProjetDto toDto(Project project);
    Project toEntity(ProjetDto dto);

    List<ProjetDto> toDtoList(List<Project> projets);
    List<Project> toEntityList(List<ProjetDto> dtos);
}
