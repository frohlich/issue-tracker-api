package com.frohlich.it.service.impl;

import com.frohlich.it.service.IssueService;
import com.frohlich.it.domain.Issue;
import com.frohlich.it.repository.IssueRepository;
import com.frohlich.it.repository.search.IssueSearchRepository;
import com.frohlich.it.service.dto.IssueDTO;
import com.frohlich.it.service.mapper.IssueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Issue.
 */
@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final Logger log = LoggerFactory.getLogger(IssueServiceImpl.class);

    private IssueRepository issueRepository;

    private IssueMapper issueMapper;

    private IssueSearchRepository issueSearchRepository;

    public IssueServiceImpl(IssueRepository issueRepository, IssueMapper issueMapper, IssueSearchRepository issueSearchRepository) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
        this.issueSearchRepository = issueSearchRepository;
    }

    /**
     * Save a issue.
     *
     * @param issueDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IssueDTO save(IssueDTO issueDTO) {
        log.debug("Request to save Issue : {}", issueDTO);

        Issue issue = issueMapper.toEntity(issueDTO);
        issue = issueRepository.save(issue);
        IssueDTO result = issueMapper.toDto(issue);
        issueSearchRepository.save(issue);
        return result;
    }

    /**
     * Get all the issues.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IssueDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Issues");
        return issueRepository.findAll(pageable)
            .map(issueMapper::toDto);
    }


    /**
     * Get one issue by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IssueDTO> findOne(Long id) {
        log.debug("Request to get Issue : {}", id);
        return issueRepository.findById(id)
            .map(issueMapper::toDto);
    }

    /**
     * Delete the issue by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Issue : {}", id);
        issueRepository.deleteById(id);
        issueSearchRepository.deleteById(id);
    }

    /**
     * Search for the issue corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IssueDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Issues for query {}", query);
        return issueSearchRepository.search(queryStringQuery(query), pageable)
            .map(issueMapper::toDto);
    }
}