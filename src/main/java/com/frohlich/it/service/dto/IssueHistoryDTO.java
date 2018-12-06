package com.frohlich.it.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.frohlich.it.domain.enumeration.Flow;

/**
 * A DTO for the IssueHistory entity.
 */
public class IssueHistoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

    private Flow flowStart;

    private Flow flowEnd;

    private String createdBy;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private String lastModifiedBy;

    private Long issueId;

    private Long commentId;
    
    private String commentDescription;
    
    private List<AttachmentDTO> attachments;
    
    public List<AttachmentDTO> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<AttachmentDTO> attachments) {
		this.attachments = attachments;
	}

	public String getCommentDescription() {
		return commentDescription;
	}

	public void setCommentDescription(String commentDescription) {
		this.commentDescription = commentDescription;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flow getFlowStart() {
        return flowStart;
    }

    public void setFlowStart(Flow flowStart) {
        this.flowStart = flowStart;
    }

    public Flow getFlowEnd() {
        return flowEnd;
    }

    public void setFlowEnd(Flow flowEnd) {
        this.flowEnd = flowEnd;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueHistoryDTO issueHistoryDTO = (IssueHistoryDTO) o;
        if (issueHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueHistoryDTO{" +
            "id=" + getId() +
            ", flowStart='" + getFlowStart() + "'" +
            ", flowEnd='" + getFlowEnd() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", issue=" + getIssueId() +
            ", comment=" + getCommentId() +
            "}";
    }
}
