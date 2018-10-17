package com.frohlich.it.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of IssueSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class IssueSearchRepositoryMockConfiguration {

    @MockBean
    private IssueSearchRepository mockIssueSearchRepository;

}
