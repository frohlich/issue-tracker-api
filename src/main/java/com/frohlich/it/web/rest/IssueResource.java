package com.frohlich.it.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.codahale.metrics.annotation.Timed;
import com.frohlich.it.domain.enumeration.Flow;
import com.frohlich.it.service.AttachmentService;
import com.frohlich.it.service.IssueService;
import com.frohlich.it.service.dto.AttachmentDTO;
import com.frohlich.it.service.dto.CommentDTO;
import com.frohlich.it.service.dto.IssueDTO;
import com.frohlich.it.service.dto.IssueHistoryDTO;
import com.frohlich.it.service.impl.FileStorageService;
import com.frohlich.it.web.rest.errors.BadRequestAlertException;
import com.frohlich.it.web.rest.util.HeaderUtil;
import com.frohlich.it.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Issue.
 */
@RestController
@RequestMapping("/api")
public class IssueResource {

    private final Logger log = LoggerFactory.getLogger(IssueResource.class);

    private static final String ENTITY_NAME = "issue";

    private IssueService issueService;
    private FileStorageService fileStorageService;
    private AttachmentService attachmentService;

    public IssueResource(IssueService issueService, FileStorageService fileStorageService, AttachmentService attachmentService) {
        this.issueService = issueService;
        this.fileStorageService = fileStorageService;
        this.attachmentService = attachmentService;
    }

    /**
     * POST  /issues : Create a new issue.
     *
     * @param issueDTO the issueDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issueDTO, or with status 400 (Bad Request) if the issue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/issues")
    @Timed
    public ResponseEntity<IssueDTO> createIssue(@Valid @RequestBody IssueDTO issueDTO) throws URISyntaxException {
        log.debug("REST request to save Issue : {}", issueDTO);
        if (issueDTO.getId() != null) {
            throw new BadRequestAlertException("A new issue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssueDTO result = issueService.save(issueDTO);
        return ResponseEntity.created(new URI("/api/issues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issues : Updates an existing issue.
     *
     * @param issueDTO the issueDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issueDTO,
     * or with status 400 (Bad Request) if the issueDTO is not valid,
     * or with status 500 (Internal Server Error) if the issueDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/issues")
    @Timed
    public ResponseEntity<IssueDTO> updateIssue(@Valid @RequestBody IssueDTO issueDTO) throws URISyntaxException {
        log.debug("REST request to update Issue : {}", issueDTO);
        if (issueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IssueDTO result = issueService.save(issueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, issueDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /issues : get all the issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of issues in body
     */
    @GetMapping("/issues")
    @Timed
    public ResponseEntity<List<IssueDTO>> getAllIssues(Pageable pageable) {
        log.debug("REST request to get a page of Issues");
        Page<IssueDTO> page = issueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /issues/:id : get the "id" issue.
     *
     * @param id the id of the issueDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issueDTO, or with status 404 (Not Found)
     */
    @GetMapping("/issues/{id}")
    @Timed
    public ResponseEntity<IssueDTO> getIssue(@PathVariable Long id) {
        log.debug("REST request to get Issue : {}", id);
        Optional<IssueDTO> issueDTO = issueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issueDTO);
    }

    /**
     * DELETE  /issues/:id : delete the "id" issue.
     *
     * @param id the id of the issueDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/issues/{id}")
    @Timed
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        log.debug("REST request to delete Issue : {}", id);
        issueService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/issues?query=:query : search for the issue corresponding
     * to the query.
     *
     * @param query the query of the issue search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/issues")
    @Timed
    public ResponseEntity<List<IssueDTO>> searchIssues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Issues for query {}", query);
        Page<IssueDTO> page = issueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private AttachmentDTO uploadFile(MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/downloadFile/")
            .path(fileName)
            .toUriString();
        
        AttachmentDTO dto = new AttachmentDTO();
        
        dto.setHash(file.getContentType());
        dto.setFilename(fileName);
        // dto.setSize(file.getSize());
        return dto;
        
        /*return new UploadFileResponse(fileName, fileDownloadUri,
            file.getContentType(), file.getSize());*/
    }

    @PostMapping("/issues/{issueId}/flow")
    public IssueHistoryDTO flow(
        @PathVariable Long issueId,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("comment") String comment) {

        log.debug(" UPLOAD :" + comment);
        // log.debug(" FLOW ID:" + flowId.toString());
        log.debug(" ISSUE ID:" + issueId.toString());
        log.debug(" Attachments number:" + String.valueOf(files.length));

        if (issueId.equals(null)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ArrayList<AttachmentDTO> attachs = new ArrayList<AttachmentDTO>();
        for (int i = 0; i < files.length; i++) {
        	attachs.add(this.uploadFile(files[i]));
		}
        
        CommentDTO dto = new CommentDTO();
        dto.setComment(comment);
        dto.setIssueId(issueId);

        return this.issueService.flowNext(issueId, dto, attachs);
     
        /*return Arrays.asList(files)
            .stream()
            .map(file -> uploadFile(file))
            .collect(Collectors.toList());
    */
    }
    
    @PostMapping("/issues/{issueId}/cancel")
    public IssueHistoryDTO cancel(
        @PathVariable Long issueId,
        @RequestParam("files") MultipartFile[] files,
        @RequestParam("comment") String comment) {

        log.debug(" UPLOAD :" + comment);
        // log.debug(" FLOW ID:" + flowId.toString());
        log.debug(" ISSUE ID:" + issueId.toString());
        log.debug(" Attachments number:" + String.valueOf(files.length));

        if (issueId.equals(null)) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        ArrayList<AttachmentDTO> attachs = new ArrayList<AttachmentDTO>();
        for (int i = 0; i < files.length; i++) {
        	attachs.add(this.uploadFile(files[i]));
		}
        
        CommentDTO dto = new CommentDTO();
        dto.setComment(comment);
        dto.setIssueId(issueId);

        return this.issueService.cancel(issueId, dto, attachs);
    }
	
    /**
     * POST /issues/:id/flowtesttofinished
     *
     * @param idIssue    the id of the entity
     * @param commentDTO new comment
     */

    @PostMapping("/issues/{idIssue}/flowtesttofinished")
    public void flowTestToFinished(@PathVariable Long idIssue, @Valid @RequestBody CommentDTO commentDTO) {
        final Optional<IssueDTO> issue = this.issueService.findOne(idIssue);

        if (!issue.isPresent()) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        this.issueService.flowTo(idIssue, Flow.FINISHED, commentDTO);
    }

    /**
     * POST /issues/:id/changeowner/:idUser
     *
     * @param idIssue the id the entity.
     * @param idUser  the id of new user.
     */
    @PostMapping("/issues/:id/changeowner/:idUser")
    public void changeOwner(@PathVariable Long idIssue, @PathVariable Long idUser) {

        if (idIssue.equals(null) || idUser.equals(null)) {
            throw new UnsupportedOperationException("Id is required.");
        }

        this.issueService.changeOwner(idIssue, idUser);
    }

}
