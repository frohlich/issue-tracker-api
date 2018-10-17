package com.frohlich.it.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CommitSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CommitSearchRepositoryMockConfiguration {

    @MockBean
    private CommitSearchRepository mockCommitSearchRepository;

}
