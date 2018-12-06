package com.frohlich.it.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.codahale.metrics.annotation.Timed;
import com.frohlich.it.domain.Attachment;
import com.frohlich.it.repository.AttachmentRepository;
import com.frohlich.it.service.AttachmentService;
import com.frohlich.it.service.dto.AttachmentDTO;
import com.frohlich.it.service.impl.FileStorageService;
import com.frohlich.it.service.mapper.AttachmentMapper;
import com.frohlich.it.web.rest.errors.BadRequestAlertException;
import com.frohlich.it.web.rest.util.HeaderUtil;
import com.frohlich.it.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Attachment.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private static final String ENTITY_NAME = "attachment";

    private AttachmentService attachmentService;
    
    private AttachmentRepository attachmentRepository;
    
    private AttachmentMapper attachmentMapper;
    
    private FileStorageService fileStorageService;

    public AttachmentResource(AttachmentService attachmentService, AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, FileStorageService fileStorageService) {
        this.attachmentService = attachmentService;
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.fileStorageService = fileStorageService;
    }

    /**
     * POST  /attachments : Create a new attachment.
     *
     * @param attachmentDTO the attachmentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new attachmentDTO, or with status 400 (Bad Request) if the attachment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/attachments")
    @Timed
    public ResponseEntity<AttachmentDTO> createAttachment(@RequestBody AttachmentDTO attachmentDTO) throws URISyntaxException {
        log.debug("REST request to save Attachment : {}", attachmentDTO);
        if (attachmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new attachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachmentDTO result = attachmentService.save(attachmentDTO);
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attachments : Updates an existing attachment.
     *
     * @param attachmentDTO the attachmentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated attachmentDTO,
     * or with status 400 (Bad Request) if the attachmentDTO is not valid,
     * or with status 500 (Internal Server Error) if the attachmentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/attachments")
    @Timed
    public ResponseEntity<AttachmentDTO> updateAttachment(@RequestBody AttachmentDTO attachmentDTO) throws URISyntaxException {
        log.debug("REST request to update Attachment : {}", attachmentDTO);
        if (attachmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AttachmentDTO result = attachmentService.save(attachmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, attachmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachments : get all the attachments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of attachments in body
     */
    @GetMapping("/attachments")
    @Timed
    public ResponseEntity<List<AttachmentDTO>> getAllAttachments(Pageable pageable) {
        log.debug("REST request to get a page of Attachments");
        Page<AttachmentDTO> page = attachmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /attachments/:id : get the "id" attachment.
     *
     * @param id the id of the attachmentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the attachmentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<AttachmentDTO> getAttachment(@PathVariable Long id) {
        log.debug("REST request to get Attachment : {}", id);
        Optional<AttachmentDTO> attachmentDTO = attachmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachmentDTO);
    }

    /**
     * DELETE  /attachments/:id : delete the "id" attachment.
     *
     * @param id the id of the attachmentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/attachments/{id}")
    @Timed
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        log.debug("REST request to delete Attachment : {}", id);
        attachmentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/attachments?query=:query : search for the attachment corresponding
     * to the query.
     *
     * @param query the query of the attachment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/attachments")
    @Timed
    public ResponseEntity<List<AttachmentDTO>> searchAttachments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Attachments for query {}", query);
        Page<AttachmentDTO> page = attachmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/attachments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/attachments/bycommentid/{commentId}")
    @Timed
    public ResponseEntity<List<AttachmentDTO>> byissueid (@PathVariable Long commentId) {
    	List<Attachment> all = this.attachmentRepository.findByCommentId(commentId);
    	List<AttachmentDTO> result = new ArrayList<AttachmentDTO>();
    	
    	for(Attachment item : all) {
    		result.add(this.attachmentMapper.toDto(item));
    	}
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping("/attachments/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id, HttpServletRequest request) {
    	
    	Optional<AttachmentDTO> attach = this.attachmentService.findOne(id);
        
        Resource resource = this.fileStorageService.loadFileAsResource(attach.get().getFilename());

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
        	log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
