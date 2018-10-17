package com.frohlich.it.repository.search;

import com.frohlich.it.domain.IssueHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the IssueHistory entity.
 */
public interface IssueHistorySearchRepository extends ElasticsearchRepository<IssueHistory, Long> {
}
