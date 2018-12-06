package com.frohlich.it.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.frohlich.it.domain.enumeration.Flow;
import com.frohlich.it.service.dto.AttachmentDTO;
import com.frohlich.it.service.dto.CommentDTO;
import com.frohlich.it.service.dto.IssueDTO;
import com.frohlich.it.service.dto.IssueHistoryDTO;

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

    IssueHistoryDTO flowNext(Long idIssue, CommentDTO commentDTO, List<AttachmentDTO> attachs);

    void flowTo(Long idIssue, Flow flow, CommentDTO commentDTO);

    void changeOwner(Long idIssue, Long idNewUser);

    void changeReporter(Long idIssue, Long idNewUser);

    void changePriority(Long idIssue, String newPriority);

    void changeType(Long idIssue, String newType);

    IssueHistoryDTO cancel(Long idIssue, CommentDTO commentDTO, List<AttachmentDTO> attachs);
}
