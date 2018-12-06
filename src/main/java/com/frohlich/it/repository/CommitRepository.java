package com.frohlich.it.repository;

import com.frohlich.it.domain.Commit;
import com.frohlich.it.domain.IssueHistory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Commit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommitRepository extends JpaRepository<Commit, Long> {

    @Query("select commit from Commit commit where commit.ownedBy.login = ?#{principal.username}")
    List<Commit> findByOwnedByIsCurrentUser();
    
    @Query("select u from #{#entityName} u where u.issue.id = ?1")
    List<Commit> findByIssueId(Long issueId);
    
    @Query("SELECT COUNT(u) FROM #{#entityName} u")
    Long countRegisters();

}
