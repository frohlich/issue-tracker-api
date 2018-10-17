package com.frohlich.it.service.impl;

import com.frohlich.it.service.AttachmentService;
import com.frohlich.it.domain.Attachment;
import com.frohlich.it.repository.AttachmentRepository;
import com.frohlich.it.repository.search.AttachmentSearchRepository;
import com.frohlich.it.service.dto.AttachmentDTO;
import com.frohlich.it.service.mapper.AttachmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Attachment.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private AttachmentRepository attachmentRepository;

    private AttachmentMapper attachmentMapper;

    private AttachmentSearchRepository attachmentSearchRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, AttachmentSearchRepository attachmentSearchRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.attachmentSearchRepository = attachmentSearchRepository;
    }

    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public AttachmentDTO save(AttachmentDTO attachmentDTO) {
        log.debug("Request to save Attachment : {}", attachmentDTO);

        Attachment attachment = attachmentMapper.toEntity(attachmentDTO);
        attachment = attachmentRepository.save(attachment);
        AttachmentDTO result = attachmentMapper.toDto(attachment);
        attachmentSearchRepository.save(attachment);
        return result;
    }

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAll(pageable)
            .map(attachmentMapper::toDto);
    }


    /**
     * Get one attachment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AttachmentDTO> findOne(Long id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findById(id)
            .map(attachmentMapper::toDto);
    }

    /**
     * Delete the attachment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Attachment : {}", id);
        attachmentRepository.deleteById(id);
        attachmentSearchRepository.deleteById(id);
    }

    /**
     * Search for the attachment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AttachmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Attachments for query {}", query);
        return attachmentSearchRepository.search(queryStringQuery(query), pageable)
            .map(attachmentMapper::toDto);
    }
}
