package com.frohlich.it.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.frohlich.it.domain.enumeration.Flow;
import com.frohlich.it.domain.enumeration.IssueType;
import com.frohlich.it.domain.enumeration.Priority;

/**
 * A DTO for the Issue entity.
 */
public class IssueDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String title;

    @Size(max = 2000)
    private String description;

    private Flow status;

    private IssueType type;

    private Priority priority;

    private Instant duoDate;

    private Instant closedAt;

    private String createdBy;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private String lastModifiedBy;

    private Long issueId;

    private Long closedById;

    private String closedByLogin;

    private Long assignedToId;

    private String assignedToLogin;

    private Long reportedById;

    private String reportedByLogin;

    private Long projectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Flow getStatus() {
        return status;
    }

    public void setStatus(Flow status) {
        this.status = status;
    }

    public IssueType getType() {
        return type;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getDuoDate() {
        return duoDate;
    }

    public void setDuoDate(Instant duoDate) {
        this.duoDate = duoDate;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
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

    public Long getClosedById() {
        return closedById;
    }

    public void setClosedById(Long userId) {
        this.closedById = userId;
    }

    public String getClosedByLogin() {
        return closedByLogin;
    }

    public void setClosedByLogin(String userLogin) {
        this.closedByLogin = userLogin;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long userId) {
        this.assignedToId = userId;
    }

    public String getAssignedToLogin() {
        return assignedToLogin;
    }

    public void setAssignedToLogin(String userLogin) {
        this.assignedToLogin = userLogin;
    }

    public Long getReportedById() {
        return reportedById;
    }

    public void setReportedById(Long userId) {
        this.reportedById = userId;
    }

    public String getReportedByLogin() {
        return reportedByLogin;
    }

    public void setReportedByLogin(String userLogin) {
        this.reportedByLogin = userLogin;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IssueDTO issueDTO = (IssueDTO) o;
        if (issueDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", priority='" + getPriority() + "'" +
            ", duoDate='" + getDuoDate() + "'" +
            ", closedAt='" + getClosedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", issue=" + getIssueId() +
            ", closedBy=" + getClosedById() +
            ", closedBy='" + getClosedByLogin() + "'" +
            ", assignedTo=" + getAssignedToId() +
            ", assignedTo='" + getAssignedToLogin() + "'" +
            ", reportedBy=" + getReportedById() +
            ", reportedBy='" + getReportedByLogin() + "'" +
            ", project=" + getProjectId() +
            "}";
    }
}
