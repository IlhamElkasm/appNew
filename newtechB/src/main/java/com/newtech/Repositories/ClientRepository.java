package com.newtech.Repositories;

import com.newtech.Enum.UserRole;
import com.newtech.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Custom methods for Client if needed
    List<Client> findAllByRole(UserRole role);
    long countByCreatedAtAfter(LocalDateTime date);


}
