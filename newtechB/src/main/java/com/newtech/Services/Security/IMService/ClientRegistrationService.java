package com.newtech.Services.Security.IMService;

import com.newtech.DTO.ClientRegistrationRequest;
import com.newtech.DTO.UserDTO;
import com.newtech.Enum.UserRole;
import com.newtech.Exception.EmailAlreadyExistsException;
import com.newtech.Model.Client;
import com.newtech.Repositories.ClientRepository;
import com.newtech.Repositories.UserRepository;
import com.newtech.Services.Security.Service.EmailClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientRegistrationService {

    private final UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private  EmailClientService emailService;
    private Set<UserRole> roles = new HashSet<>();

    public ClientRegistrationService(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
       ;
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

        // Send confirmation email
        emailService.sendRegistrationConfirmationEmail(savedClient.getEmail(), savedClient.getNom());

        return mapToDTO(savedClient);
    }


    public List<UserDTO> getAllClients() {
        List<Client> clients = clientRepository.findAllByRole(UserRole.CLIENT);
        return clients.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private UserDTO mapToDTO(Client client) {
        UserDTO dto = new UserDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setRoles(client.getRoles());
        dto.setRole(UserRole.CLIENT);
        dto.setActive(client.isActive());
        dto.setTelephone(client.getTelephone()); // 👈 Assurez-vous que ces getters existent
        dto.setAdresse(client.getAdresse());
        return dto;
    }

    public long countNewClients() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);
        return clientRepository.countByCreatedAtAfter(lastMonth);
    }

}


