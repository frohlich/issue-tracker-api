package com.frohlich.it.service;

import com.frohlich.it.service.dto.ProjectDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Project.
 */
public interface ProjectService {

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    ProjectDTO save(ProjectDTO projectDTO);

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProjectDTO> findAll(Pageable pageable);


    /**
     * Get the "id" project.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProjectDTO> findOne(Long id);

    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Suspend the "id" project.
     *
     * @param id the id of the entity
     */
    void suspend(Long id);


    /**
     * Search for the project corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ProjectDTO> search(String query, Pageable pageable);
}
