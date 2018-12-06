package com.frohlich.it.repository;

import com.frohlich.it.domain.Attachment;
import com.frohlich.it.domain.Issue;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Issue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("select issue from Issue issue where issue.closedBy.login = ?#{principal.username}")
    List<Issue> findByClosedByIsCurrentUser();

    @Query("select issue from Issue issue where issue.assignedTo.login = ?#{principal.username}")
    List<Issue> findByAssignedToIsCurrentUser();

    @Query("select issue from Issue issue where issue.reportedBy.login = ?#{principal.username}")
    List<Issue> findByReportedByIsCurrentUser();
    
    @Query("select u from #{#entityName} u where u.project.id = ?1")
    List<Issue> findByProjectId(Long projectId);
    
    @Query("SELECT COUNT(u) FROM #{#entityName} u")
    Long countRegisters();

}
