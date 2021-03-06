package com.frohlich.it.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Commit.
 */
@Entity
@Table(name = "commit")
@Document(indexName = "commit")
public class Commit extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "jhi_hash", nullable = false)
    private String hash;

    @ManyToOne
    @JsonIgnoreProperties("commits")
    private Issue issue;

    /**
     * Um commit está associado a algum usuário do sistema
     */
    @ApiModelProperty(value = "Um commit está associado a algum usuário do sistema")
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

    public String getHash() {
        return hash;
    }

    public Commit hash(String hash) {
        this.hash = hash;
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Issue getIssue() {
        return issue;
    }

    public Commit issue(Issue issue) {
        this.issue = issue;
        return this;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    public User getOwnedBy() {
        return ownedBy;
    }

    public Commit ownedBy(User user) {
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
        Commit commit = (Commit) o;
        if (commit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Commit{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
