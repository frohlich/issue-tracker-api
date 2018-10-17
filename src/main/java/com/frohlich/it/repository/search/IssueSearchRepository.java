package com.frohlich.it.repository.search;

import com.frohlich.it.domain.Issue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Issue entity.
 */
public interface IssueSearchRepository extends ElasticsearchRepository<Issue, Long> {
}
