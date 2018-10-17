package com.frohlich.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.frohlich.it.domain.enumeration.Flow;

/**
 * A IssueHistory.
 */
@Entity
@Table(name = "issue_history")
@Document(indexName = "issuehistory")
public class IssueHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "flow_start")
    private Flow flowStart;

    @Enumerated(EnumType.STRING)
    @Column(name = "flow_end")
    private Flow flowEnd;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    /**
     * Uma issue pode estar associada a mais de um histórico
     */
    @ApiModelProperty(value = "Uma issue pode estar associada a mais de um histórico")
    @ManyToOne
    @JsonIgnoreProperties("")
    private Issue issue;

    /**
     * Um commentário pode estar associada a mais de um histórico
     */
    @ApiModelProperty(value = "Um commentário pode estar associada a mais de um histórico")
    @ManyToOne
    @JsonIgnoreProperties("")
    private Comment comment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Flow getFlowStart() {
        return flowStart;
    }

    public IssueHistory flowStart(Flow flowStart) {
        this.flowStart = flowStart;
        return this;
    }

    public void setFlowStart(Flow flowStart) {
        this.flowStart = flowStart;
    }

    public Flow getFlowEnd() {
        return flowEnd;
    }

    public IssueHistory flowEnd(Flow flowEnd) {
        this.flowEnd = flowEnd;
        return this;
    }

    public void setFlowEnd(Flow flowEnd) {
        this.flowEnd = flowEnd;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public IssueHistory createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public IssueHistory createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public IssueHistory lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public IssueHistory lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Issue getIssue() {
        return issue;
    }

    public IssueHistory issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Comment getComment() {
        return comment;
    }

    public IssueHistory comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IssueHistory issueHistory = (IssueHistory) o;
        if (issueHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issueHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IssueHistory{" +
            "id=" + getId() +
            ", flowStart='" + getFlowStart() + "'" +
            ", flowEnd='" + getFlowEnd() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
