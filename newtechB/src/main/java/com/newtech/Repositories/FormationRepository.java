package com.newtech.Repositories;

import com.newtech.Model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {
    // Add custom queries here if necessary
}
