package com.frohlich.it.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.frohlich.it.domain.IssueHistory;


/**
 * Spring Data  repository for the IssueHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueHistoryRepository extends JpaRepository<IssueHistory, Long> {
	  @Query("select u from #{#entityName} u where u.issue.id = ?1")
	  List<IssueHistory> findByIssueId(Long issueId);
}
