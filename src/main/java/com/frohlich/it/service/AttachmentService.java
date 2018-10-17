package com.frohlich.it.service;

import com.frohlich.it.service.dto.AttachmentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Attachment.
 */
public interface AttachmentService {

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save
     * @return the persisted entity
     */
    AttachmentDTO save(AttachmentDTO attachmentDTO);

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AttachmentDTO> findAll(Pageable pageable);


    /**
     * Get the "id" attachment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<AttachmentDTO> findOne(Long id);

    /**
     * Delete the "id" attachment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the attachment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<AttachmentDTO> search(String query, Pageable pageable);
}
