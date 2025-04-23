package com.newtech.Controllers;

import com.newtech.DTO.FormationDto;
import com.newtech.Services.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/formations")  // Changed from "/api"
@CrossOrigin("*")
public class FormationController {
    @Autowired
    private FormationService formationService;

    @PostMapping("/admin/create")
    public ResponseEntity<FormationDto> createFormation(@RequestBody FormationDto formationDto) {
        FormationDto createdFormation = formationService.createFormation(formationDto);
        return new ResponseEntity<>(createdFormation, HttpStatus.CREATED);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<FormationDto> updateFormation(@PathVariable Long id, @RequestBody FormationDto formationDto) {
        FormationDto updatedFormation = formationService.updateFormation(id, formationDto);
        return updatedFormation != null ? ResponseEntity.ok(updatedFormation) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormationDto> getFormation(@PathVariable Long id) {
        FormationDto formation = formationService.getFormation(id);
        return formation != null ? ResponseEntity.ok(formation) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<FormationDto>> getAllFormations() {
        List<FormationDto> formations = formationService.getAllFormations();
        return ResponseEntity.ok(formations);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        return formationService.deleteFormation(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}