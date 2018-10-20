package com.frohlich.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.frohlich.it.domain.enumeration.Flow;
import com.frohlich.it.domain.enumeration.IssueType;
import com.frohlich.it.domain.enumeration.Priority;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Issue.
 */
@Entity
@Table(name = "issue")
@Document(indexName = "issue")
public class Issue extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Flow status;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private IssueType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "duo_date")
    private Instant duoDate;

    @Column(name = "closed_at")
    private Instant closedAt;

    /**
     * Issue tem vários commits associados
     */
    @ApiModelProperty(value = "Issue tem vários commits associados")
    @OneToMany(mappedBy = "issue")
    private Set<Commit> commits = new HashSet<>();
    /**
     * Issue pode ter vários comentários
     */
    @ApiModelProperty(value = "Issue pode ter vários comentários")
    @OneToMany(mappedBy = "issue")
    private Set<Comment> comments = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("parents")
    private Issue issue;

    /**
     * Issue pode ter um pai, uma associação
     */
    @ApiModelProperty(value = "Issue pode ter um pai, uma associação")
    @OneToMany(mappedBy = "issue")
    private Set<Issue> parents = new HashSet<>();
    /**
     * Issue encerrada por
     */
    @ApiModelProperty(value = "Issue encerrada por")
    @ManyToOne
    @JsonIgnoreProperties("")
    private User closedBy;

    /**
     * Issue designada para
     */
    @ApiModelProperty(value = "Issue designada para")
    @ManyToOne
    @JsonIgnoreProperties("")
    private User assignedTo;

    /**
     * Issue criada por
     */
    @ApiModelProperty(value = "Issue criada por")
    @ManyToOne
    @JsonIgnoreProperties("")
    private User reportedBy;

    @ManyToOne
    @JsonIgnoreProperties("issues")
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Issue title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Issue description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Flow getStatus() {
        return status;
    }

    public Issue status(Flow status) {
        this.status = status;
        return this;
    }

    public void setStatus(Flow status) {
        this.status = status;
    }

    public IssueType getType() {
        return type;
    }

    public Issue type(IssueType type) {
        this.type = type;
        return this;
    }

    public void setType(IssueType type) {
        this.type = type;
    }

    public Priority getPriority() {
        return priority;
    }

    public Issue priority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getDuoDate() {
        return duoDate;
    }

    public Issue duoDate(Instant duoDate) {
        this.duoDate = duoDate;
        return this;
    }

    public void setDuoDate(Instant duoDate) {
        this.duoDate = duoDate;
    }

    public Instant getClosedAt() {
        return closedAt;
    }

    public Issue closedAt(Instant closedAt) {
        this.closedAt = closedAt;
        return this;
    }

    public void setClosedAt(Instant closedAt) {
        this.closedAt = closedAt;
    }

    public Set<Commit> getCommits() {
        return commits;
    }

    public Issue commits(Set<Commit> commits) {
        this.commits = commits;
        return this;
    }

    public Issue addCommits(Commit commit) {
        this.commits.add(commit);
        commit.setIssue(this);
        return this;
    }

    public Issue removeCommits(Commit commit) {
        this.commits.remove(commit);
        commit.setIssue(null);
        return this;
    }

    public void setCommits(Set<Commit> commits) {
        this.commits = commits;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Issue comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Issue addComments(Comment comment) {
        this.comments.add(comment);
        comment.setIssue(this);
        return this;
    }

    public Issue removeComments(Comment comment) {
        this.comments.remove(comment);
        comment.setIssue(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Issue getIssue() {
        return issue;
    }

    public Issue issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public Set<Issue> getParents() {
        return parents;
    }

    public Issue parents(Set<Issue> issues) {
        this.parents = issues;
        return this;
    }

    public Issue addParent(Issue issue) {
        this.parents.add(issue);
        issue.setIssue(this);
        return this;
    }

    public Issue removeParent(Issue issue) {
        this.parents.remove(issue);
        issue.setIssue(null);
        return this;
    }

    public void setParents(Set<Issue> issues) {
        this.parents = issues;
    }

    public User getClosedBy() {
        return closedBy;
    }

    public Issue closedBy(User user) {
        this.closedBy = user;
        return this;
    }

    public void setClosedBy(User user) {
        this.closedBy = user;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public Issue assignedTo(User user) {
        this.assignedTo = user;
        return this;
    }

    public void setAssignedTo(User user) {
        this.assignedTo = user;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public Issue reportedBy(User user) {
        this.reportedBy = user;
        return this;
    }

    public void setReportedBy(User user) {
        this.reportedBy = user;
    }

    public Project getProject() {
        return project;
    }

    public Issue project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
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
        Issue issue = (Issue) o;
        if (issue.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), issue.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Issue{" +
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
            "}";
    }
}
