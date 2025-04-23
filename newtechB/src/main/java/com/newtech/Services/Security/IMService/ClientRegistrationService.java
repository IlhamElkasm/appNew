package com.newtech.Services.Security.IMService;

import com.newtech.DTO.ClientRegistrationRequest;
import com.newtech.DTO.UserDTO;
import com.newtech.Enum.UserRole;
import com.newtech.Exception.EmailAlreadyExistsException;
import com.newtech.Model.Client;
import com.newtech.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class ClientRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private Set<UserRole> roles = new HashSet<>();


    public ClientRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO registerClient(ClientRegistrationRequest request) throws EmailAlreadyExistsException {
        log.info("Creating new client with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        // Create client
        Client client = new Client();
        client.setNom(request.getNom());
        client.setEmail(request.getEmail());
        client.setMotDePasse(passwordEncoder.encode(request.getPassword()));
        client.setRole(UserRole.valueOf(UserRole.CLIENT.name()));
        client.setRoles(Collections.singleton(UserRole.CLIENT));


        // Set client-specific fields
        client.setTelephone(request.getTelephone());
        client.setAdresse(request.getAdresse());

        Client savedClient = userRepository.save(client);
        log.info("Client created successfully with ID: {}", savedClient.getId());

        return mapToDTO(savedClient);
    }

    private UserDTO mapToDTO(Client client) {
        UserDTO dto = new UserDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setRole(client.getRole());
        dto.setRoles(client.getRoles());
        dto.setActive(client.isActive());
        return dto;
    }
}


