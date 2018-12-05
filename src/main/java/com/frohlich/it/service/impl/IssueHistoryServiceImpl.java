package com.frohlich.it.service.impl;

import com.frohlich.it.service.IssueHistoryService;
import com.frohlich.it.domain.Issue;
import com.frohlich.it.domain.IssueHistory;
import com.frohlich.it.repository.IssueHistoryRepository;
import com.frohlich.it.repository.search.IssueHistorySearchRepository;
import com.frohlich.it.service.dto.IssueHistoryDTO;
import com.frohlich.it.service.mapper.IssueHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing IssueHistory.
 */
@Service
@Transactional
public class IssueHistoryServiceImpl implements IssueHistoryService {

    private final Logger log = LoggerFactory.getLogger(IssueHistoryServiceImpl.class);

    private IssueHistoryRepository issueHistoryRepository;

    private IssueHistoryMapper issueHistoryMapper;

    private IssueHistorySearchRepository issueHistorySearchRepository;

    public IssueHistoryServiceImpl(IssueHistoryRepository issueHistoryRepository, IssueHistoryMapper issueHistoryMapper, IssueHistorySearchRepository issueHistorySearchRepository) {
        this.issueHistoryRepository = issueHistoryRepository;
        this.issueHistoryMapper = issueHistoryMapper;
        this.issueHistorySearchRepository = issueHistorySearchRepository;
    }

    /**
     * Save a issueHistory.
     *
     * @param issueHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IssueHistoryDTO save(IssueHistoryDTO issueHistoryDTO) {
        log.debug("Request to save IssueHistory : {}", issueHistoryDTO);

        IssueHistory issueHistory = issueHistoryMapper.toEntity(issueHistoryDTO);
        issueHistory = issueHistoryRepository.save(issueHistory);
        IssueHistoryDTO result = issueHistoryMapper.toDto(issueHistory);
        issueHistorySearchRepository.save(issueHistory);
        return result;
    }

    /**
     * Get all the issueHistories.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IssueHistoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IssueHistories");
        return issueHistoryRepository.findAll(pageable)
            .map(issueHistoryMapper::toDto);
    }


    /**
     * Get one issueHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<IssueHistoryDTO> findOne(Long id) {
        log.debug("Request to get IssueHistory : {}", id);
        return issueHistoryRepository.findById(id)
            .map(issueHistoryMapper::toDto);
    }

    /**
     * Delete the issueHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IssueHistory : {}", id);
        issueHistoryRepository.deleteById(id);
        issueHistorySearchRepository.deleteById(id);
    }

    /**
     * Search for the issueHistory corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IssueHistoryDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of IssueHistories for query {}", query);
        return issueHistorySearchRepository.search(queryStringQuery(query), pageable)
            .map(issueHistoryMapper::toDto);
    }

	@Override
	public List<IssueHistoryDTO> findByIssueId(Long issueId) {
		IssueHistory ih = new IssueHistory();
		Issue is = new Issue();
		is.setId(issueId);
		ih.setIssue(is);
		Example<IssueHistory> eih = Example.of(ih);
		
		List<IssueHistory> lista = this.issueHistoryRepository.findAll(eih);
		List<IssueHistoryDTO> result = new ArrayList<IssueHistoryDTO>();
		
		for (IssueHistory item : lista) {
			result.add(issueHistoryMapper.toDto(item));
		}
		
		return result;
	}

}
