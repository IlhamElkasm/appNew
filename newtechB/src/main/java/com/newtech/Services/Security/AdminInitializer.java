package com.newtech.Services.Security;

import com.newtech.DTO.BaseRegistrationRequest;
import com.newtech.Enum.UserRole;
import com.newtech.Model.Admin;
import com.newtech.Repositories.UserRepository;
import com.newtech.Services.Security.IMService.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

@Configuration
@Slf4j
public class AdminInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initializeAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            boolean adminExists = userRepository.findAll().stream()
                    .anyMatch(user -> user.getRoles().contains(UserRole.ADMIN));

            if (!adminExists) {
                log.info("Aucun administrateur trouvé. Création de l'administrateur par défaut...");

                // Create Admin entity directly
                Admin admin = new Admin();
                admin.setNom("newtechemultimedia");
                admin.setEmail("newtechemultimedia@gmail.com");
                admin.setMotDePasse(passwordEncoder.encode("newTeche@25"));
                admin.setRole(UserRole.ADMIN);
                admin.setRoles(Collections.singleton(UserRole.ADMIN));

                try {
                    userRepository.save(admin);
                    log.info("Administrateur par défaut créé avec succès");
                } catch (Exception e) {
                    log.error("Erreur lors de la création de l'administrateur par défaut", e);
                }
            } else {
                log.info("Un administrateur existe déjà. Aucun besoin d'en créer un nouveau.");
            }
        };
    }
}
