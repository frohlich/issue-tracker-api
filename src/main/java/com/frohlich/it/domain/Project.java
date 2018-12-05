
package com.frohlich.it.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Document(indexName = "project")
public class Project extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "suspended")
    private Boolean suspended;

    /**
     * Um projeto tem várias issues
     */
    @ApiModelProperty(value = "Um projeto tem várias issues")
    @OneToMany(mappedBy = "project")
    private Set<Issue> issues = new HashSet<>();
    /**
     * Projeto iniciado por
     */
    @ApiModelProperty(value = "Projeto iniciado por")
    @ManyToOne
    @JsonIgnoreProperties("")
    private User ownedBy;

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

    public Project title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean isSuspended() {
        return suspended;
    }

    public Project suspended(Boolean suspended) {
        this.suspended = suspended;
        return this;
    }

    public void setSuspended(Boolean suspended) {
        this.suspended = suspended;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public Project issues(Set<Issue> issues) {
        this.issues = issues;
        return this;
    }

    public Project addIssues(Issue issue) {
        this.issues.add(issue);
        issue.setProject(this);
        return this;
    }

    public Project removeIssues(Issue issue) {
        this.issues.remove(issue);
        issue.setProject(null);
        return this;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    public User getOwnedBy() {
        return ownedBy;
    }

    public Project ownedBy(User user) {
        this.ownedBy = user;
        return this;
    }

    public void setOwnedBy(User user) {
        this.ownedBy = user;
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
        Project project = (Project) o;
        if (project.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), project.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", suspended='" + isSuspended() + "'" +
            "}";
    }
}
