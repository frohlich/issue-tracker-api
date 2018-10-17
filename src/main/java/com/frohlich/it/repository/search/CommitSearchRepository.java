package com.frohlich.it.repository.search;

import com.frohlich.it.domain.Commit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Commit entity.
 */
public interface CommitSearchRepository extends ElasticsearchRepository<Commit, Long> {
}
