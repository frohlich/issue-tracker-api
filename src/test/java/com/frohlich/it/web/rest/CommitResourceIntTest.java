package com.frohlich.it.web.rest;

import com.frohlich.it.IssueTrackerApp;

import com.frohlich.it.domain.Commit;
import com.frohlich.it.repository.CommitRepository;
import com.frohlich.it.repository.search.CommitSearchRepository;
import com.frohlich.it.service.CommitService;
import com.frohlich.it.service.dto.CommitDTO;
import com.frohlich.it.service.mapper.CommitMapper;
import com.frohlich.it.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.frohlich.it.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CommitResource REST controller.
 *
 * @see CommitResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IssueTrackerApp.class)
public class CommitResourceIntTest {

    private static final String DEFAULT_HASH = "AAAAAAAAAA";
    private static final String UPDATED_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private CommitMapper commitMapper;
    
    @Autowired
    private CommitService commitService;

    /**
     * This repository is mocked in the com.frohlich.it.repository.search test package.
     *
     * @see com.frohlich.it.repository.search.CommitSearchRepositoryMockConfiguration
     */
    @Autowired
    private CommitSearchRepository mockCommitSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCommitMockMvc;

    private Commit commit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommitResource commitResource = new CommitResource(commitService);
        this.restCommitMockMvc = MockMvcBuilders.standaloneSetup(commitResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commit createEntity(EntityManager em) {
        Commit commit = new Commit()
            .hash(DEFAULT_HASH)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return commit;
    }

    @Before
    public void initTest() {
        commit = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommit() throws Exception {
        int databaseSizeBeforeCreate = commitRepository.findAll().size();

        // Create the Commit
        CommitDTO commitDTO = commitMapper.toDto(commit);
        restCommitMockMvc.perform(post("/api/commits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commitDTO)))
            .andExpect(status().isCreated());

        // Validate the Commit in the database
        List<Commit> commitList = commitRepository.findAll();
        assertThat(commitList).hasSize(databaseSizeBeforeCreate + 1);
        Commit testCommit = commitList.get(commitList.size() - 1);
        assertThat(testCommit.getHash()).isEqualTo(DEFAULT_HASH);
        assertThat(testCommit.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testCommit.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testCommit.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testCommit.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the Commit in Elasticsearch
        verify(mockCommitSearchRepository, times(1)).save(testCommit);
    }

    @Test
    @Transactional
    public void createCommitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commitRepository.findAll().size();

        // Create the Commit with an existing ID
        commit.setId(1L);
        CommitDTO commitDTO = commitMapper.toDto(commit);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommitMockMvc.perform(post("/api/commits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commit in the database
        List<Commit> commitList = commitRepository.findAll();
        assertThat(commitList).hasSize(databaseSizeBeforeCreate);

        // Validate the Commit in Elasticsearch
        verify(mockCommitSearchRepository, times(0)).save(commit);
    }

    @Test
    @Transactional
    public void checkHashIsRequired() throws Exception {
        int databaseSizeBeforeTest = commitRepository.findAll().size();
        // set the field null
        commit.setHash(null);

        // Create the Commit, which fails.
        CommitDTO commitDTO = commitMapper.toDto(commit);

        restCommitMockMvc.perform(post("/api/commits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commitDTO)))
            .andExpect(status().isBadRequest());

        List<Commit> commitList = commitRepository.findAll();
        assertThat(commitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCommits() throws Exception {
        // Initialize the database
        commitRepository.saveAndFlush(commit);

        // Get all the commitList
        restCommitMockMvc.perform(get("/api/commits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commit.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }
    
    @Test
    @Transactional
    public void getCommit() throws Exception {
        // Initialize the database
        commitRepository.saveAndFlush(commit);

        // Get the commit
        restCommitMockMvc.perform(get("/api/commits/{id}", commit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commit.getId().intValue()))
            .andExpect(jsonPath("$.hash").value(DEFAULT_HASH.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommit() throws Exception {
        // Get the commit
        restCommitMockMvc.perform(get("/api/commits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommit() throws Exception {
        // Initialize the database
        commitRepository.saveAndFlush(commit);

        int databaseSizeBeforeUpdate = commitRepository.findAll().size();

        // Update the commit
        Commit updatedCommit = commitRepository.findById(commit.getId()).get();
        // Disconnect from session so that the updates on updatedCommit are not directly saved in db
        em.detach(updatedCommit);
        updatedCommit
            .hash(UPDATED_HASH)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        CommitDTO commitDTO = commitMapper.toDto(updatedCommit);

        restCommitMockMvc.perform(put("/api/commits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commitDTO)))
            .andExpect(status().isOk());

        // Validate the Commit in the database
        List<Commit> commitList = commitRepository.findAll();
        assertThat(commitList).hasSize(databaseSizeBeforeUpdate);
        Commit testCommit = commitList.get(commitList.size() - 1);
        assertThat(testCommit.getHash()).isEqualTo(UPDATED_HASH);
        assertThat(testCommit.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testCommit.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testCommit.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testCommit.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the Commit in Elasticsearch
        verify(mockCommitSearchRepository, times(1)).save(testCommit);
    }

    @Test
    @Transactional
    public void updateNonExistingCommit() throws Exception {
        int databaseSizeBeforeUpdate = commitRepository.findAll().size();

        // Create the Commit
        CommitDTO commitDTO = commitMapper.toDto(commit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommitMockMvc.perform(put("/api/commits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Commit in the database
        List<Commit> commitList = commitRepository.findAll();
        assertThat(commitList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Commit in Elasticsearch
        verify(mockCommitSearchRepository, times(0)).save(commit);
    }

    @Test
    @Transactional
    public void deleteCommit() throws Exception {
        // Initialize the database
        commitRepository.saveAndFlush(commit);

        int databaseSizeBeforeDelete = commitRepository.findAll().size();

        // Get the commit
        restCommitMockMvc.perform(delete("/api/commits/{id}", commit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Commit> commitList = commitRepository.findAll();
        assertThat(commitList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Commit in Elasticsearch
        verify(mockCommitSearchRepository, times(1)).deleteById(commit.getId());
    }

    @Test
    @Transactional
    public void searchCommit() throws Exception {
        // Initialize the database
        commitRepository.saveAndFlush(commit);
        when(mockCommitSearchRepository.search(queryStringQuery("id:" + commit.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(commit), PageRequest.of(0, 1), 1));
        // Search the commit
        restCommitMockMvc.perform(get("/api/_search/commits?query=id:" + commit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commit.getId().intValue())))
            .andExpect(jsonPath("$.[*].hash").value(hasItem(DEFAULT_HASH.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commit.class);
        Commit commit1 = new Commit();
        commit1.setId(1L);
        Commit commit2 = new Commit();
        commit2.setId(commit1.getId());
        assertThat(commit1).isEqualTo(commit2);
        commit2.setId(2L);
        assertThat(commit1).isNotEqualTo(commit2);
        commit1.setId(null);
        assertThat(commit1).isNotEqualTo(commit2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommitDTO.class);
        CommitDTO commitDTO1 = new CommitDTO();
        commitDTO1.setId(1L);
        CommitDTO commitDTO2 = new CommitDTO();
        assertThat(commitDTO1).isNotEqualTo(commitDTO2);
        commitDTO2.setId(commitDTO1.getId());
        assertThat(commitDTO1).isEqualTo(commitDTO2);
        commitDTO2.setId(2L);
        assertThat(commitDTO1).isNotEqualTo(commitDTO2);
        commitDTO1.setId(null);
        assertThat(commitDTO1).isNotEqualTo(commitDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(commitMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(commitMapper.fromId(null)).isNull();
    }
}
