package com.frohlich.it.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of IssueHistorySearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IssueHistorySearchRepositoryMockConfiguration {

    @MockBean
    private IssueHistorySearchRepository mockIssueHistorySearchRepository;

}
