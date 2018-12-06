package com.frohlich.it.repository;

import com.frohlich.it.domain.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("select project from Project project where project.ownedBy.login = ?#{principal.username}")
    List<Project> findByOwnedByIsCurrentUser();

    @Query("SELECT COUNT(u) FROM #{#entityName} u")
    Long countRegisters();
    
}
