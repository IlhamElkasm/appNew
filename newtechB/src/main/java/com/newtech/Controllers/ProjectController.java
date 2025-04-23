package com.newtech.Controllers;

import com.newtech.DTO.ProjetDto;
import com.newtech.Services.ProjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")  // Changed from "/api"
@CrossOrigin("*")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/admin/create")
    public ResponseEntity<ProjetDto> createProject(@RequestBody ProjetDto projetDto) {
        return ResponseEntity.ok(projectService.createProject(projetDto));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjetDto>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<ProjetDto> getProjectById(@PathVariable Long id) {
        ProjetDto projetDto = projectService.getProjectById(id);
        return projetDto != null ? ResponseEntity.ok(projetDto) : ResponseEntity.notFound().build();
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<ProjetDto> updateProject(@PathVariable Long id, @RequestBody ProjetDto projetDto) {
        ProjetDto updatedProject = projectService.updateProject(id, projetDto);
        return updatedProject != null ? ResponseEntity.ok(updatedProject) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        return projectService.deleteProject(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}