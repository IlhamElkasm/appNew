package com.newtech.Services.Security.IMService;


import com.newtech.DTO.SecretaireRegistrationRequest;
import com.newtech.DTO.UserDTO;
import com.newtech.Enum.UserRole;
import com.newtech.Exception.EmailAlreadyExistsException;
import com.newtech.Model.Secretaire;
import com.newtech.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class SecretaireRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private Set<UserRole> roles = new HashSet<>();


    public SecretaireRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO registerSecretaire(SecretaireRegistrationRequest request) throws EmailAlreadyExistsException {
        log.info("Creating new secretary with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Create secretary
        Secretaire secretaire = new Secretaire();
        secretaire.setNom(request.getNom());
        secretaire.setEmail(request.getEmail());
        secretaire.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        secretaire.setRole(UserRole.valueOf(UserRole.SECRETAIRE.name()));

        // ✅ الحل هنا: حفظ `Set<UserRole>` بدلاً من `Set<String>`
        secretaire.setRoles(Collections.singleton(UserRole.SECRETAIRE));

        Secretaire savedSecretaire = userRepository.save(secretaire);
        log.info("Secretary created successfully with ID: {}", savedSecretaire.getId());

        return mapToDTO(savedSecretaire);
    }


    private UserDTO mapToDTO(Secretaire secretaire) {
        UserDTO dto = new UserDTO();
        dto.setId(secretaire.getId());
        dto.setNom(secretaire.getNom());
        dto.setEmail(secretaire.getEmail());
        dto.setRole(secretaire.getRole());
        dto.setRoles(secretaire.getRoles());
        dto.setActive(secretaire.isActive());
        return dto;
    }
}
