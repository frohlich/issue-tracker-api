package com.frohlich.it.web.rest;

import com.frohlich.it.IssueTrackerApp;

import com.frohlich.it.domain.IssueHistory;
import com.frohlich.it.repository.IssueHistoryRepository;
import com.frohlich.it.repository.search.IssueHistorySearchRepository;
import com.frohlich.it.service.IssueHistoryService;
import com.frohlich.it.service.dto.IssueHistoryDTO;
import com.frohlich.it.service.mapper.IssueHistoryMapper;
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
import com.frohlich.it.domain.enumeration.Flow;
/**
 * Test class for the IssueHistoryResource REST controller.
 *
 * @see IssueHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = IssueTrackerApp.class)
public class IssueHistoryResourceIntTest {

    private static final Flow DEFAULT_FLOW_START = Flow.BACKLOG;
    private static final Flow UPDATED_FLOW_START = Flow.SPECIFICATION;

    private static final Flow DEFAULT_FLOW_END = Flow.BACKLOG;
    private static final Flow UPDATED_FLOW_END = Flow.SPECIFICATION;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private IssueHistoryRepository issueHistoryRepository;

    @Autowired
    private IssueHistoryMapper issueHistoryMapper;
    
    @Autowired
    private IssueHistoryService issueHistoryService;

    /**
     * This repository is mocked in the com.frohlich.it.repository.search test package.
     *
     * @see com.frohlich.it.repository.search.IssueHistorySearchRepositoryMockConfiguration
     */
    @Autowired
    private IssueHistorySearchRepository mockIssueHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIssueHistoryMockMvc;

    private IssueHistory issueHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IssueHistoryResource issueHistoryResource = new IssueHistoryResource(issueHistoryService);
        this.restIssueHistoryMockMvc = MockMvcBuilders.standaloneSetup(issueHistoryResource)
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
    public static IssueHistory createEntity(EntityManager em) {
        IssueHistory issueHistory = new IssueHistory()
            .flowStart(DEFAULT_FLOW_START)
            .flowEnd(DEFAULT_FLOW_END)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return issueHistory;
    }

    @Before
    public void initTest() {
        issueHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssueHistory() throws Exception {
        int databaseSizeBeforeCreate = issueHistoryRepository.findAll().size();

        // Create the IssueHistory
        IssueHistoryDTO issueHistoryDTO = issueHistoryMapper.toDto(issueHistory);
        restIssueHistoryMockMvc.perform(post("/api/issue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the IssueHistory in the database
        List<IssueHistory> issueHistoryList = issueHistoryRepository.findAll();
        assertThat(issueHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        IssueHistory testIssueHistory = issueHistoryList.get(issueHistoryList.size() - 1);
        assertThat(testIssueHistory.getFlowStart()).isEqualTo(DEFAULT_FLOW_START);
        assertThat(testIssueHistory.getFlowEnd()).isEqualTo(DEFAULT_FLOW_END);
        assertThat(testIssueHistory.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testIssueHistory.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testIssueHistory.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testIssueHistory.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the IssueHistory in Elasticsearch
        verify(mockIssueHistorySearchRepository, times(1)).save(testIssueHistory);
    }

    @Test
    @Transactional
    public void createIssueHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = issueHistoryRepository.findAll().size();

        // Create the IssueHistory with an existing ID
        issueHistory.setId(1L);
        IssueHistoryDTO issueHistoryDTO = issueHistoryMapper.toDto(issueHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIssueHistoryMockMvc.perform(post("/api/issue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueHistory in the database
        List<IssueHistory> issueHistoryList = issueHistoryRepository.findAll();
        assertThat(issueHistoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the IssueHistory in Elasticsearch
        verify(mockIssueHistorySearchRepository, times(0)).save(issueHistory);
    }

    @Test
    @Transactional
    public void getAllIssueHistories() throws Exception {
        // Initialize the database
        issueHistoryRepository.saveAndFlush(issueHistory);

        // Get all the issueHistoryList
        restIssueHistoryMockMvc.perform(get("/api/issue-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].flowStart").value(hasItem(DEFAULT_FLOW_START.toString())))
            .andExpect(jsonPath("$.[*].flowEnd").value(hasItem(DEFAULT_FLOW_END.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }
    
    @Test
    @Transactional
    public void getIssueHistory() throws Exception {
        // Initialize the database
        issueHistoryRepository.saveAndFlush(issueHistory);

        // Get the issueHistory
        restIssueHistoryMockMvc.perform(get("/api/issue-histories/{id}", issueHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issueHistory.getId().intValue()))
            .andExpect(jsonPath("$.flowStart").value(DEFAULT_FLOW_START.toString()))
            .andExpect(jsonPath("$.flowEnd").value(DEFAULT_FLOW_END.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIssueHistory() throws Exception {
        // Get the issueHistory
        restIssueHistoryMockMvc.perform(get("/api/issue-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssueHistory() throws Exception {
        // Initialize the database
        issueHistoryRepository.saveAndFlush(issueHistory);

        int databaseSizeBeforeUpdate = issueHistoryRepository.findAll().size();

        // Update the issueHistory
        IssueHistory updatedIssueHistory = issueHistoryRepository.findById(issueHistory.getId()).get();
        // Disconnect from session so that the updates on updatedIssueHistory are not directly saved in db
        em.detach(updatedIssueHistory);
        updatedIssueHistory
            .flowStart(UPDATED_FLOW_START)
            .flowEnd(UPDATED_FLOW_END)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        IssueHistoryDTO issueHistoryDTO = issueHistoryMapper.toDto(updatedIssueHistory);

        restIssueHistoryMockMvc.perform(put("/api/issue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the IssueHistory in the database
        List<IssueHistory> issueHistoryList = issueHistoryRepository.findAll();
        assertThat(issueHistoryList).hasSize(databaseSizeBeforeUpdate);
        IssueHistory testIssueHistory = issueHistoryList.get(issueHistoryList.size() - 1);
        assertThat(testIssueHistory.getFlowStart()).isEqualTo(UPDATED_FLOW_START);
        assertThat(testIssueHistory.getFlowEnd()).isEqualTo(UPDATED_FLOW_END);
        assertThat(testIssueHistory.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testIssueHistory.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testIssueHistory.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testIssueHistory.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the IssueHistory in Elasticsearch
        verify(mockIssueHistorySearchRepository, times(1)).save(testIssueHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingIssueHistory() throws Exception {
        int databaseSizeBeforeUpdate = issueHistoryRepository.findAll().size();

        // Create the IssueHistory
        IssueHistoryDTO issueHistoryDTO = issueHistoryMapper.toDto(issueHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIssueHistoryMockMvc.perform(put("/api/issue-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(issueHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IssueHistory in the database
        List<IssueHistory> issueHistoryList = issueHistoryRepository.findAll();
        assertThat(issueHistoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the IssueHistory in Elasticsearch
        verify(mockIssueHistorySearchRepository, times(0)).save(issueHistory);
    }

    @Test
    @Transactional
    public void deleteIssueHistory() throws Exception {
        // Initialize the database
        issueHistoryRepository.saveAndFlush(issueHistory);

        int databaseSizeBeforeDelete = issueHistoryRepository.findAll().size();

        // Get the issueHistory
        restIssueHistoryMockMvc.perform(delete("/api/issue-histories/{id}", issueHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<IssueHistory> issueHistoryList = issueHistoryRepository.findAll();
        assertThat(issueHistoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IssueHistory in Elasticsearch
        verify(mockIssueHistorySearchRepository, times(1)).deleteById(issueHistory.getId());
    }

    @Test
    @Transactional
    public void searchIssueHistory() throws Exception {
        // Initialize the database
        issueHistoryRepository.saveAndFlush(issueHistory);
        when(mockIssueHistorySearchRepository.search(queryStringQuery("id:" + issueHistory.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(issueHistory), PageRequest.of(0, 1), 1));
        // Search the issueHistory
        restIssueHistoryMockMvc.perform(get("/api/_search/issue-histories?query=id:" + issueHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(issueHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].flowStart").value(hasItem(DEFAULT_FLOW_START.toString())))
            .andExpect(jsonPath("$.[*].flowEnd").value(hasItem(DEFAULT_FLOW_END.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueHistory.class);
        IssueHistory issueHistory1 = new IssueHistory();
        issueHistory1.setId(1L);
        IssueHistory issueHistory2 = new IssueHistory();
        issueHistory2.setId(issueHistory1.getId());
        assertThat(issueHistory1).isEqualTo(issueHistory2);
        issueHistory2.setId(2L);
        assertThat(issueHistory1).isNotEqualTo(issueHistory2);
        issueHistory1.setId(null);
        assertThat(issueHistory1).isNotEqualTo(issueHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IssueHistoryDTO.class);
        IssueHistoryDTO issueHistoryDTO1 = new IssueHistoryDTO();
        issueHistoryDTO1.setId(1L);
        IssueHistoryDTO issueHistoryDTO2 = new IssueHistoryDTO();
        assertThat(issueHistoryDTO1).isNotEqualTo(issueHistoryDTO2);
        issueHistoryDTO2.setId(issueHistoryDTO1.getId());
        assertThat(issueHistoryDTO1).isEqualTo(issueHistoryDTO2);
        issueHistoryDTO2.setId(2L);
        assertThat(issueHistoryDTO1).isNotEqualTo(issueHistoryDTO2);
        issueHistoryDTO1.setId(null);
        assertThat(issueHistoryDTO1).isNotEqualTo(issueHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(issueHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(issueHistoryMapper.fromId(null)).isNull();
    }
}
