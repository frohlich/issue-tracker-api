package com.frohlich.it.service;

import com.frohlich.it.service.dto.IssueDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Issue.
 */
public interface IssueService {

    /**
     * Save a issue.
     *
     * @param issueDTO the entity to save
     * @return the persisted entity
     */
    IssueDTO save(IssueDTO issueDTO);

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IssueDTO> findAll(Pageable pageable);


    /**
     * Get the "id" issue.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IssueDTO> findOne(Long id);

    /**
     * Delete the "id" issue.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the issue corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IssueDTO> search(String query, Pageable pageable);
}
