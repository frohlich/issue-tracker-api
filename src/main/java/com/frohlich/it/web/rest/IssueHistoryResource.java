package com.frohlich.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.frohlich.it.service.IssueHistoryService;
import com.frohlich.it.web.rest.errors.BadRequestAlertException;
import com.frohlich.it.web.rest.util.HeaderUtil;
import com.frohlich.it.web.rest.util.PaginationUtil;
import com.frohlich.it.service.dto.IssueHistoryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing IssueHistory.
 */
@RestController
@RequestMapping("/api")
public class IssueHistoryResource {

    private final Logger log = LoggerFactory.getLogger(IssueHistoryResource.class);

    private static final String ENTITY_NAME = "issueHistory";

    private IssueHistoryService issueHistoryService;

    public IssueHistoryResource(IssueHistoryService issueHistoryService) {
        this.issueHistoryService = issueHistoryService;
    }

    /**
     * POST  /issue-histories : Create a new issueHistory.
     *
     * @param issueHistoryDTO the issueHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issueHistoryDTO, or with status 400 (Bad Request) if the issueHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issue-histories")
    @Timed
    public ResponseEntity<IssueHistoryDTO> createIssueHistory(@RequestBody IssueHistoryDTO issueHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save IssueHistory : {}", issueHistoryDTO);
        if (issueHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new issueHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueHistoryDTO result = issueHistoryService.save(issueHistoryDTO);
        return ResponseEntity.created(new URI("/api/issue-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issue-histories : Updates an existing issueHistory.
     *
     * @param issueHistoryDTO the issueHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issueHistoryDTO,
     * or with status 400 (Bad Request) if the issueHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the issueHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issue-histories")
    @Timed
    public ResponseEntity<IssueHistoryDTO> updateIssueHistory(@RequestBody IssueHistoryDTO issueHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update IssueHistory : {}", issueHistoryDTO);
        if (issueHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueHistoryDTO result = issueHistoryService.save(issueHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issueHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issue-histories : get all the issueHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of issueHistories in body
     */
    @GetMapping("/issue-histories")
    @Timed
    public ResponseEntity<List<IssueHistoryDTO>> getAllIssueHistories(Pageable pageable) {
        log.debug("REST request to get a page of IssueHistories");
        Page<IssueHistoryDTO> page = issueHistoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issue-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /issue-histories/:id : get the "id" issueHistory.
     *
     * @param id the id of the issueHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issueHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/issue-histories/{id}")
    @Timed
    public ResponseEntity<IssueHistoryDTO> getIssueHistory(@PathVariable Long id) {
        log.debug("REST request to get IssueHistory : {}", id);
        Optional<IssueHistoryDTO> issueHistoryDTO = issueHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueHistoryDTO);
    }

    /**
     * DELETE  /issue-histories/:id : delete the "id" issueHistory.
     *
     * @param id the id of the issueHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issue-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssueHistory(@PathVariable Long id) {
        log.debug("REST request to delete IssueHistory : {}", id);
        issueHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/issue-histories?query=:query : search for the issueHistory corresponding
     * to the query.
     *
     * @param query the query of the issueHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/issue-histories")
    @Timed
    public ResponseEntity<List<IssueHistoryDTO>> searchIssueHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IssueHistories for query {}", query);
        Page<IssueHistoryDTO> page = issueHistoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/issue-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
