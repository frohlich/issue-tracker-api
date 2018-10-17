package com.frohlich.it.service.impl;

import com.frohlich.it.service.CommitService;
import com.frohlich.it.domain.Commit;
import com.frohlich.it.repository.CommitRepository;
import com.frohlich.it.repository.search.CommitSearchRepository;
import com.frohlich.it.service.dto.CommitDTO;
import com.frohlich.it.service.mapper.CommitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Commit.
 */
@Service
@Transactional
public class CommitServiceImpl implements CommitService {

    private final Logger log = LoggerFactory.getLogger(CommitServiceImpl.class);

    private CommitRepository commitRepository;

    private CommitMapper commitMapper;

    private CommitSearchRepository commitSearchRepository;

    public CommitServiceImpl(CommitRepository commitRepository, CommitMapper commitMapper, CommitSearchRepository commitSearchRepository) {
        this.commitRepository = commitRepository;
        this.commitMapper = commitMapper;
        this.commitSearchRepository = commitSearchRepository;
    }

    /**
     * Save a commit.
     *
     * @param commitDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CommitDTO save(CommitDTO commitDTO) {
        log.debug("Request to save Commit : {}", commitDTO);

        Commit commit = commitMapper.toEntity(commitDTO);
        commit = commitRepository.save(commit);
        CommitDTO result = commitMapper.toDto(commit);
        commitSearchRepository.save(commit);
        return result;
    }

    /**
     * Get all the commits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Commits");
        return commitRepository.findAll(pageable)
            .map(commitMapper::toDto);
    }


    /**
     * Get one commit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CommitDTO> findOne(Long id) {
        log.debug("Request to get Commit : {}", id);
        return commitRepository.findById(id)
            .map(commitMapper::toDto);
    }

    /**
     * Delete the commit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Commit : {}", id);
        commitRepository.deleteById(id);
        commitSearchRepository.deleteById(id);
    }

    /**
     * Search for the commit corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CommitDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Commits for query {}", query);
        return commitSearchRepository.search(queryStringQuery(query), pageable)
            .map(commitMapper::toDto);
    }
}
