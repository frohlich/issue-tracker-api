package com.frohlich.it.service.impl;

import com.frohlich.it.service.ProjectService;
import com.frohlich.it.domain.Project;
import com.frohlich.it.repository.ProjectRepository;
import com.frohlich.it.repository.search.ProjectSearchRepository;
import com.frohlich.it.service.dto.ProjectDTO;
import com.frohlich.it.service.mapper.ProjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    private final ProjectSearchRepository projectSearchRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, ProjectSearchRepository projectSearchRepository) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.projectSearchRepository = projectSearchRepository;
    }

    /**
     * Save a project.
     *
     * @param projectDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectDTO save(ProjectDTO projectDTO) {
        log.debug("Request to save Project : {}", projectDTO);

        Project project = projectMapper.toEntity(projectDTO);
        project = projectRepository.save(project);
        ProjectDTO result = projectMapper.toDto(project);
        projectSearchRepository.save(project);
        return result;
    }

    /**
     * Get all the projects.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable)
            .map(projectMapper::toDto);
    }


    /**
     * Get one project by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id)
            .map(projectMapper::toDto);
    }

    /**
     * Delete the project by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
        projectSearchRepository.deleteById(id);
    }

    /**
     * Suspend the project by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void suspend(Long id) {
        Optional<ProjectDTO> model = this.findOne(id);
        if (model.isPresent()) {
            model.get().setSuspended(true);
            this.save(model.get());
        }
    }

    /**
     * Search for the project corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Projects for query {}", query);
        return projectSearchRepository.search(queryStringQuery(query), pageable)
            .map(projectMapper::toDto);
    }
}
