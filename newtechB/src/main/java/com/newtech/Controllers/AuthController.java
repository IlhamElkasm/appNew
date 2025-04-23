//package com.newtech.Controllers;
//
//// Authentication Controller
//import com.newtech.DTO.JwtAuthenticationResponse;
//import com.newtech.DTO.LoginRequest;
//import com.newtech.DTO.UserDTO;
//import com.newtech.Exception.EmailAlreadyExistsException;
//import com.newtech.Services.Security.JwtTokenProvider;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/auth")
//@Slf4j
//@Validated
//public class AuthController {
//
//    private final AuthenticationManager authenticationManager;
//    private final UserService userService;
//    private final JwtTokenProvider tokenProvider;
//
//    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider tokenProvider) {
//        this.authenticationManager = authenticationManager;
//        this.userService = userService;
//        this.tokenProvider = tokenProvider;
//    }
//
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
//        // Extract roles from the authentication object
//        Set<String> roles = authentication.getAuthorities().stream()
//                .map(grantedAuthority -> grantedAuthority.getAuthority())
//                .collect(Collectors.toSet());
//
//        log.info("User logged in successfully: {}", loginRequest.getEmail());
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, roles));  // Return the token and roles
//    }
//
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
//        log.info("Processing registration request for user: {}", signUpRequest.getEmail());
//
//        try {
//            UserDTO createdUser = userService.createUser(signUpRequest);
//            log.info("User registered successfully: {}", createdUser.getEmail());
//            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
//        } catch (EmailAlreadyExistsException e) {
//            log.error("Registration failed. Email already exists: {}", signUpRequest.getEmail());
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//    }
//}