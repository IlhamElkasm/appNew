package com.newtech.Services;

import com.newtech.DTO.ProjetDto;
import com.newtech.Mapper.ProjetMapper;
import com.newtech.Model.Project;
import com.newtech.Repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjetMapper projetMapper;

    public ProjectService(ProjectRepository projectRepository, ProjetMapper projetMapper) {
        this.projectRepository = projectRepository;
        this.projetMapper = projetMapper;
    }

    // 🔹 Créer un projet
    public ProjetDto createProject(ProjetDto projetDto) {
        Project project = projetMapper.toEntity(projetDto);
        return projetMapper.toDto(projectRepository.save(project));
    }

    // 🔹 Récupérer tous les projets
    public List<ProjetDto> getAllProjects() {
        return projetMapper.toDtoList(projectRepository.findAll());
    }

    // 🔹 Récupérer un projet par ID
    public ProjetDto getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.map(projetMapper::toDto).orElse(null);
    }

    // 🔹 Mettre à jour un projet
    public ProjetDto updateProject(Long id, ProjetDto projetDto) {
        if (!projectRepository.existsById(id)) {
            return null;
        }
        Project project = projetMapper.toEntity(projetDto);
        project.setId(id); // S'assurer que l'ID reste le même
        return projetMapper.toDto(projectRepository.save(project));
    }

    // 🔹 Supprimer un projet
    public boolean deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            return false;
        }
        projectRepository.deleteById(id);
        return true;
    }
}


