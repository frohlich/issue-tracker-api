package com.frohlich.it.service;

import com.frohlich.it.service.dto.IssueHistoryDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing IssueHistory.
 */
public interface IssueHistoryService {

    /**
     * Save a issueHistory.
     *
     * @param issueHistoryDTO the entity to save
     * @return the persisted entity
     */
    IssueHistoryDTO save(IssueHistoryDTO issueHistoryDTO);

    /**
     * Get all the issueHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IssueHistoryDTO> findAll(Pageable pageable);


    /**
     * Get the "id" issueHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<IssueHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" issueHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the issueHistory corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IssueHistoryDTO> search(String query, Pageable pageable);
}
