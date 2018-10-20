package com.frohlich.it.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.frohlich.it.service.CommitService;
import com.frohlich.it.web.rest.errors.BadRequestAlertException;
import com.frohlich.it.web.rest.util.HeaderUtil;
import com.frohlich.it.web.rest.util.PaginationUtil;
import com.frohlich.it.service.dto.CommitDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Commit.
 */
@RestController
@RequestMapping("/api")
public class CommitResource {

    private final Logger log = LoggerFactory.getLogger(CommitResource.class);

    private static final String ENTITY_NAME = "commit";

    private CommitService commitService;

    public CommitResource(CommitService commitService) {
        this.commitService = commitService;
    }

    /**
     * POST  /commits : Create a new commit.
     *
     * @param commitDTO the commitDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commitDTO, or with status 400 (Bad Request) if the commit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/commits")
    @Timed
    public ResponseEntity<CommitDTO> createCommit(@Valid @RequestBody CommitDTO commitDTO) throws URISyntaxException {
        log.debug("REST request to save Commit : {}", commitDTO);
        if (commitDTO.getId() != null) {
            throw new BadRequestAlertException("A new commit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommitDTO result = commitService.save(commitDTO);
        return ResponseEntity.created(new URI("/api/commits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commits : get all the commits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commits in body
     */
    @GetMapping("/commits")
    @Timed
    public ResponseEntity<List<CommitDTO>> getAllCommits(Pageable pageable) {
        log.debug("REST request to get a page of Commits");
        Page<CommitDTO> page = commitService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commits/:id : get the "id" commit.
     *
     * @param id the id of the commitDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commitDTO, or with status 404 (Not Found)
     */
    @GetMapping("/commits/{id}")
    @Timed
    public ResponseEntity<CommitDTO> getCommit(@PathVariable Long id) {
        log.debug("REST request to get Commit : {}", id);
        Optional<CommitDTO> commitDTO = commitService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commitDTO);
    }

    /**
     * DELETE  /commits/:id : delete the "id" commit.
     *
     * @param id the id of the commitDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/commits/{id}")
    @Timed
    public ResponseEntity<Void> deleteCommit(@PathVariable Long id) {
        log.debug("REST request to delete Commit : {}", id);
        commitService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/commits?query=:query : search for the commit corresponding
     * to the query.
     *
     * @param query the query of the commit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/commits")
    @Timed
    public ResponseEntity<List<CommitDTO>> searchCommits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Commits for query {}", query);
        Page<CommitDTO> page = commitService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
