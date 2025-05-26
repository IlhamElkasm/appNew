package com.newtech.Controllers;

import com.newtech.DTO.*;
import com.newtech.Enum.UserRole;
import com.newtech.Exception.EmailAlreadyExistsException;
import com.newtech.Services.Security.IMService.ClientRegistrationService;
import com.newtech.Services.Security.IMService.SecretaireRegistrationService;
import com.newtech.Services.Security.JwtTokenProvider;
import com.newtech.Services.Security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@Slf4j
public class RegistrationController {


    private final ClientRegistrationService clientRegistrationService;
    private final SecretaireRegistrationService secretaireRegistrationService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public RegistrationController(ClientRegistrationService clientRegistrationService,
                                  SecretaireRegistrationService secretaireRegistrationService, JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.clientRegistrationService = clientRegistrationService;
        this.secretaireRegistrationService = secretaireRegistrationService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    // Public endpoint for client registration
    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientRegistrationRequest request) {
        log.info("Processing client registration request for: {}", request.getEmail());

        try {
            UserDTO createdUser = clientRegistrationService.registerClient(request);
            log.info("Client registered successfully: {}", createdUser.getEmail());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (EmailAlreadyExistsException e) {
            log.error("Registration failed. Email already exists: {}", request.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Secured endpoint for secretary registration - only admins can register secretaries
    @PostMapping("/register/secretaire")
    public ResponseEntity<?> registerSecretaire(@Valid @RequestBody SecretaireRegistrationRequest request) {
        log.info("Processing secretary registration request for: {}", request.getEmail());

        try {
            UserDTO createdUser = secretaireRegistrationService.registerSecretaire(request);
            log.info("Secretary registered successfully: {}", createdUser.getEmail());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (EmailAlreadyExistsException e) {
            log.error("Registration failed. Email already exists: {}", request.getEmail());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    // Dans RegistrationController.java - Éliminer le remplacement
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Processing login request for user: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        // Simplement collecter les autorités sans modification
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        log.info("User logged in successfully: {}", loginRequest.getEmail());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, roles));
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        log.info("Processing login request for user: {}", loginRequest.getEmail());
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(authentication);
//
//        // Récupération de l'utilisateur connecté
//        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//        // Ajout de log pour debugging
//        log.info("User principal nom: {}", userPrincipal.getNom());
//
//        Set<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toSet());
//
//        log.info("User logged in successfully: {}", loginRequest.getEmail());
//
//        // Création de la réponse avec le nom utilisateur
//        JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, "Bearer", roles, userPrincipal.getNom());
//        log.info("Auth response contains nom: {}", response.getNom());
//
//        return ResponseEntity.ok(response);
//    }



    @GetMapping("/clients")
    @PreAuthorize("hasRole('ADMIN')") // Optionnel
    public ResponseEntity<List<UserDTO>> getAllClients() {
        List<UserDTO> clients = clientRegistrationService.getAllClients();
        return ResponseEntity.ok(clients);
    }


    @GetMapping("/clients/new/count")
    public ResponseEntity<Long> countNewClients() {
        long newClientsCount = clientRegistrationService.countNewClients();
        return ResponseEntity.ok(newClientsCount);
    }


}
