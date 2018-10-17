package com.frohlich.it.repository;

import com.frohlich.it.domain.IssueHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the IssueHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {

}
