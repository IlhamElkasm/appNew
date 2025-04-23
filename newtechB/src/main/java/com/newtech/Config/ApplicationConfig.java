//package com.newtech.Config;
//
//import com.newtech.Enum.UserRole;
//import com.newtech.Model.User;
//import com.newtech.Repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class ApplicationConfig {
//
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public ApplicationConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//    }
//
//    @Bean
//    public CommandLineRunner initializeAdmin() {
//        return args -> {
//            // Vérifier si un admin existe déjà
//            if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
//                // Créer un admin par défaut
//                User admin = User.builder()
//                        .name("Admin")
//                        .email("admin@admin.com")
//                        .password(passwordEncoder.encode("admin123"))
//                        .role(UserRole.ADMIN)
//                        .enabled(true)
//                        .build();
//
//                userRepository.save(admin);
//                System.out.println("Admin par défaut créé avec email: admin@admin.com et mot de passe: admin123");
//            }
//        };
//    }
//    @Configuration
//    public class EncoderConfig {
//        @Bean
//        @Lazy
//        public PasswordEncoder passwordEncoder() {
//            return new BCryptPasswordEncoder();
//        }
//    }
//}
