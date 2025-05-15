package com.newtech.Services;


import com.newtech.DTO.FormationDto;
import com.newtech.Mapper.FormationMapper;
import com.newtech.Model.Formation;
import com.newtech.Repositories.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FormationService {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private FormationMapper formationMapper;

    public FormationDto createFormation(FormationDto formationDto) {
        Formation formation = formationMapper.toEntity(formationDto);
        formation = formationRepository.save(formation);
        return formationMapper.toDto(formation);
    }

    public FormationDto updateFormation(Long id, FormationDto formationDto) {
        if (formationRepository.existsById(id)) {
            Formation formation = formationMapper.toEntity(formationDto);
            formation.setId(id);
            formation = formationRepository.save(formation);
            return formationMapper.toDto(formation);
        }
        return null;
    }

    public FormationDto getFormation(Long id) {
        return formationRepository.findById(id)
                .map(formationMapper::toDto)
                .orElse(null);
    }

    public List<FormationDto> getAllFormations() {
        List<Formation> formations = formationRepository.findAll();
        return formationMapper.toDtoList(formations);
    }

    public boolean deleteFormation(Long id) {
        if (formationRepository.existsById(id)) {
            formationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long countFormations() {
        return formationRepository.count();
    }




}
