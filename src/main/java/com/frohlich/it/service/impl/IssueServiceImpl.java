package com.frohlich.it.service.impl;

import com.frohlich.it.domain.User;
import com.frohlich.it.domain.enumeration.Flow;
import com.frohlich.it.domain.enumeration.IssueType;
import com.frohlich.it.domain.enumeration.Priority;
import com.frohlich.it.service.CommentService;
import com.frohlich.it.service.IssueService;
import com.frohlich.it.domain.Issue;
import com.frohlich.it.repository.IssueRepository;
import com.frohlich.it.repository.search.IssueSearchRepository;
import com.frohlich.it.service.UserService;
import com.frohlich.it.service.dto.CommentDTO;
import com.frohlich.it.service.dto.IssueDTO;
import com.frohlich.it.service.mapper.IssueMapper;
import com.frohlich.it.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
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

    private UserService userService;

    private CommentService commentService;

    public IssueServiceImpl(IssueRepository issueRepository, IssueMapper issueMapper,
                            IssueSearchRepository issueSearchRepository, UserService userService,
                            CommentService commentService) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
        this.issueSearchRepository = issueSearchRepository;
        this.userService = userService;
        this.commentService = commentService;
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

    /**
     * Advance Flow.BACKLOG to Flow.SPECIFICATION.
     *
     * @param idIssue the id of the entity.
     * @param commentDTO new comment.
     */
    @Transactional
    public void flowTo (Long idIssue, Flow flow, CommentDTO commentDTO) {
        final Optional<Issue> issue = issueRepository.findById(idIssue);

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        issue.get().setStatus(flow);

        // TODO: Send Mail;

        this.commentService.save(commentDTO);

        this.issueRepository.save(issue.get());
    }

    @Transactional @Override
    public void changeOwner(Long idIssue, Long idNewUser) {
        final Optional<Issue> issue = issueRepository.findById(idIssue);

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        final Optional<User> user = userService.getUserWithAuthorities(idNewUser);

        if (!user.isPresent()) {
            throw new BadRequestAlertException("User is invalid", Issue.class.getName(), "");
        }

        issue.get().setAssignedTo(user.get());

        this.issueRepository.save(issue.get());

    }

    @Transactional @Override
    public void changeReporter(Long idIssue, Long idNewUser) {
        final Optional<Issue> issue = issueRepository.findById(idIssue);

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        final Optional<User> user = userService.getUserWithAuthorities(idNewUser);

        if (!user.isPresent()) {
            throw new BadRequestAlertException("User is invalid", Issue.class.getName(), "");
        }

        issue.get().setReportedBy(user.get());

        this.issueRepository.save(issue.get());
    }

    @Transactional @Override
    public void changePriority(Long idIssue, String newPriority) {
        final Optional<Issue> issue = issueRepository.findById(idIssue);

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        if (!Priority.contains(newPriority)) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        issue.get().setPriority(Priority.valueOf(newPriority));

        this.issueRepository.save(issue.get());
    }

    @Override
    public void changeType(Long idIssue, String newType) {
        final Optional<Issue> issue = issueRepository.findById(idIssue);

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        if (!IssueType.contains(newType)) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        issue.get().setType(IssueType.valueOf(newType));

        this.issueRepository.save(issue.get());
    }

    @Override @Transactional
    public void cancel(Long idIssue, CommentDTO commentDTO) {
        final Optional<Issue> issue = issueRepository.findById(idIssue);
        final Optional<User> user = userService.getUserWithAuthorities();

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid parameter", Issue.class.getName(), "");
        }

        this.commentService.save(commentDTO);

        issue.get().setClosedBy(user.get());
        issue.get().setClosedAt(Instant.now());
        issue.get().setStatus(Flow.CANCELED);

        this.issueRepository.save(issue.get());
    }
}
