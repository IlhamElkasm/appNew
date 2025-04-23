package com.newtech.Services.Security.IMService;


import com.newtech.DTO.BaseRegistrationRequest;
import com.newtech.Enum.UserRole;
import com.newtech.Exception.EmailAlreadyExistsException;
import com.newtech.Model.User;
import com.newtech.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void createUser(BaseRegistrationRequest request) throws EmailAlreadyExistsException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setMotDePasse(passwordEncoder.encode(request.getPassword()));

        // Convert String roles to UserRole enums
        Set<UserRole> userRoles = new HashSet<>();
        if (request.getRoles() != null) {
            for (String role : request.getRoles()) {
                try {
                    userRoles.add(UserRole.valueOf(role.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Handle invalid role values if needed
                }
            }
        }
        user.setRoles(userRoles);

        // Set the first role as the primary role (or a default)
        user.setRole(userRoles.isEmpty() ? UserRole.CLIENT : userRoles.iterator().next());

        userRepository.save(user);
    }
}