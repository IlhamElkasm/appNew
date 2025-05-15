//package com.newtech.Controllers;
//
//import com.newtech.DTO.ClientProfileDTO;
//import com.newtech.DTO.ClientProfileUpdateDTO;
//
//import com.newtech.Services.Security.ClientProfileService;
//import com.newtech.Services.Security.UserPrincipal;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/client/profile")
//@CrossOrigin("*")
//@Slf4j
//public class ClientProfileController {
//
//    private final ClientProfileService clientProfileService;
//
//    public ClientProfileController(ClientProfileService clientProfileService) {
//        this.clientProfileService = clientProfileService;
//    }
//
//    @GetMapping
//    @PreAuthorize("hasRole('CLIENT')")
//    public ResponseEntity<ClientProfileDTO> getClientProfile(@AuthenticationPrincipal UserPrincipal currentUser) {
//        log.info("GET request to fetch profile for client with ID: {}", currentUser.getUserId());
//
//        ClientProfileDTO profileDTO = clientProfileService.getClientProfile(currentUser.getUserId());
//        return ResponseEntity.ok(profileDTO);
//    }
//
//    @PutMapping
//    @PreAuthorize("hasRole('CLIENT')")
//    public ResponseEntity<ClientProfileDTO> updateClientProfile(
//            @AuthenticationPrincipal UserPrincipal currentUser,
//            @Valid @RequestBody ClientProfileUpdateDTO updateDTO) {
//
//        log.info("PUT request to update profile for client with ID: {}", currentUser.getUserId());
//
//        ClientProfileDTO updatedProfile = clientProfileService.updateClientProfile(
//                currentUser.getUserId(), updateDTO);
//
//        return ResponseEntity.ok(updatedProfile);
//    }
//
//    @DeleteMapping
//    @PreAuthorize("hasRole('CLIENT')")
//    public ResponseEntity<?> deleteClientAccount(@AuthenticationPrincipal UserPrincipal currentUser) {
//        log.info("DELETE request to remove account for client with ID: {}", currentUser.getUserId());
//
//        clientProfileService.deleteClientAccount(currentUser.getUserId());
//
//        return ResponseEntity.ok().build();
//    }
//}
