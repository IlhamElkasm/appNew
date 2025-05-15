package com.newtech.Services.Security;


import com.newtech.DTO.ClientProfileDTO;
import com.newtech.DTO.ClientProfileUpdateDTO;
import com.newtech.Exception.ResourceNotFoundException;
import com.newtech.Model.Client;
import com.newtech.Repositories.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ClientProfileService {

    private final ClientRepository clientRepository;

    public ClientProfileService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientProfileDTO getClientProfile(Long clientId) {
        log.info("Fetching profile for client with ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        return mapToProfileDTO(client);
    }

    @Transactional
    public ClientProfileDTO updateClientProfile(Long clientId, ClientProfileUpdateDTO updateDTO) {
        log.info("Updating profile for client with ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        client.setNom(updateDTO.getNom());
        client.setTelephone(updateDTO.getTelephone());
        client.setAdresse(updateDTO.getAdresse());

        Client updatedClient = clientRepository.save(client);
        log.info("Profile updated successfully for client with ID: {}", clientId);

        return mapToProfileDTO(updatedClient);
    }

    @Transactional
    public void deleteClientAccount(Long clientId) {
        log.info("Deleting account for client with ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + clientId));

        clientRepository.delete(client);
        log.info("Client account deleted successfully with ID: {}", clientId);
    }

    private ClientProfileDTO mapToProfileDTO(Client client) {
        ClientProfileDTO dto = new ClientProfileDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setEmail(client.getEmail());
        dto.setTelephone(client.getTelephone());
        dto.setAdresse(client.getAdresse());
        return dto;
    }
}
