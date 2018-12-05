package com.frohlich.it.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.frohlich.it.domain.enumeration.Flow;

import io.swagger.annotations.ApiModelProperty;

/**
 * A IssueHistory.
 */
@Entity
@Table(name = "issue_history")
@Document(indexName = "issuehistory")
public class IssueHistory extends AbstractAuditingEntity implements Serializable {

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
