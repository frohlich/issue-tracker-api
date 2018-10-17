package com.frohlich.it.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of AttachmentSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AttachmentSearchRepositoryMockConfiguration {

    @MockBean
    private AttachmentSearchRepository mockAttachmentSearchRepository;

}
