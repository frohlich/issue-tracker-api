package com.frohlich.it.web.rest;

import com.frohlich.it.IssueTrackerApp;

import com.frohlich.it.domain.Issue;
import com.frohlich.it.repository.IssueRepository;
import com.frohlich.it.repository.search.IssueSearchRepository;
import com.frohlich.it.service.IssueService;
import com.frohlich.it.service.dto.IssueDTO;
import com.frohlich.it.service.mapper.IssueMapper;
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

import com.frohlich.it.domain.enumeration.Flow;
import com.frohlich.it.domain.enumeration.IssueType;
import com.frohlich.it.domain.enumeration.Priority;
/**
 * Test class for the IssueResource REST controller.
 *
 * @see IssueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IssueTrackerApp.class)
public class IssueResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Flow DEFAULT_STATUS = Flow.BACKLOG;
    private static final Flow UPDATED_STATUS = Flow.SPECIFICATION;

    private static final IssueType DEFAULT_TYPE = IssueType.STORY;
    private static final IssueType UPDATED_TYPE = IssueType.BUG;

    private static final Priority DEFAULT_PRIORITY = Priority.CRITICAL;
    private static final Priority UPDATED_PRIORITY = Priority.HIGH;

    private static final Instant DEFAULT_DUO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueMapper issueMapper;
    
    @Autowired
    private IssueService issueService;

    /**
     * This repository is mocked in the com.frohlich.it.repository.search test package.
     *
     * @see com.frohlich.it.repository.search.IssueSearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueSearchRepository mockIssueSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueMockMvc;

    private Issue issue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueResource issueResource = new IssueResource(issueService);
        this.restIssueMockMvc = MockMvcBuilders.standaloneSetup(issueResource)
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
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE)
            .priority(DEFAULT_PRIORITY)
            .duoDate(DEFAULT_DUO_DATE)
            .closedAt(DEFAULT_CLOSED_AT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return issue;
    }

    @Before
    public void initTest() {
        issue = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);
        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testIssue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIssue.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testIssue.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIssue.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testIssue.getDuoDate()).isEqualTo(DEFAULT_DUO_DATE);
        assertThat(testIssue.getClosedAt()).isEqualTo(DEFAULT_CLOSED_AT);
        assertThat(testIssue.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIssue.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIssue.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIssue.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(1)).save(testIssue);
    }

    @Test
    @Transactional
    public void createIssueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue with an existing ID
        issue.setId(1L);
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeCreate);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(0)).save(issue);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = issueRepository.findAll().size();
        // set the field null
        issue.setTitle(null);

        // Create the Issue, which fails.
        IssueDTO issueDTO = issueMapper.toDto(issue);

        restIssueMockMvc.perform(post("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllIssues() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issueList
        restIssueMockMvc.perform(get("/api/issues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].duoDate").value(hasItem(DEFAULT_DUO_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }
    
    @Test
    @Transactional
    public void getIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issue.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.duoDate").value(DEFAULT_DUO_DATE.toString()))
            .andExpect(jsonPath("$.closedAt").value(DEFAULT_CLOSED_AT.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIssue() throws Exception {
        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findById(issue.getId()).get();
        // Disconnect from session so that the updates on updatedIssue are not directly saved in db
        em.detach(updatedIssue);
        updatedIssue
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .duoDate(UPDATED_DUO_DATE)
            .closedAt(UPDATED_CLOSED_AT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        IssueDTO issueDTO = issueMapper.toDto(updatedIssue);

        restIssueMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issueList.get(issueList.size() - 1);
        assertThat(testIssue.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testIssue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssue.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testIssue.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIssue.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testIssue.getDuoDate()).isEqualTo(UPDATED_DUO_DATE);
        assertThat(testIssue.getClosedAt()).isEqualTo(UPDATED_CLOSED_AT);
        assertThat(testIssue.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIssue.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIssue.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIssue.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(1)).save(testIssue);
    }

    @Test
    @Transactional
    public void updateNonExistingIssue() throws Exception {
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Create the Issue
        IssueDTO issueDTO = issueMapper.toDto(issue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssueMockMvc.perform(put("/api/issues")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Issue in the database
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(0)).save(issue);
    }

    @Test
    @Transactional
    public void deleteIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        int databaseSizeBeforeDelete = issueRepository.findAll().size();

        // Get the issue
        restIssueMockMvc.perform(delete("/api/issues/{id}", issue.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Issue> issueList = issueRepository.findAll();
        assertThat(issueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Issue in Elasticsearch
        verify(mockIssueSearchRepository, times(1)).deleteById(issue.getId());
    }

    @Test
    @Transactional
    public void searchIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        when(mockIssueSearchRepository.search(queryStringQuery("id:" + issue.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(issue), PageRequest.of(0, 1), 1));
        // Search the issue
        restIssueMockMvc.perform(get("/api/_search/issues?query=id:" + issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].duoDate").value(hasItem(DEFAULT_DUO_DATE.toString())))
            .andExpect(jsonPath("$.[*].closedAt").value(hasItem(DEFAULT_CLOSED_AT.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Issue.class);
        Issue issue1 = new Issue();
        issue1.setId(1L);
        Issue issue2 = new Issue();
        issue2.setId(issue1.getId());
        assertThat(issue1).isEqualTo(issue2);
        issue2.setId(2L);
        assertThat(issue1).isNotEqualTo(issue2);
        issue1.setId(null);
        assertThat(issue1).isNotEqualTo(issue2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueDTO.class);
        IssueDTO issueDTO1 = new IssueDTO();
        issueDTO1.setId(1L);
        IssueDTO issueDTO2 = new IssueDTO();
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO2.setId(issueDTO1.getId());
        assertThat(issueDTO1).isEqualTo(issueDTO2);
        issueDTO2.setId(2L);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
        issueDTO1.setId(null);
        assertThat(issueDTO1).isNotEqualTo(issueDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueMapper.fromId(null)).isNull();
    }
}
