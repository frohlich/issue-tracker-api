package com.frohlich.it.repository;

import com.frohlich.it.domain.Attachment;
import com.frohlich.it.domain.IssueHistory;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Attachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
	  @Query("select u from #{#entityName} u where u.comment.id = ?1")
	  List<Attachment> findByCommentId(Long commentId);
}
