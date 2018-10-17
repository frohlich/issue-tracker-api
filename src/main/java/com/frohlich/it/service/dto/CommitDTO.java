package com.frohlich.it.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Commit entity.
 */
public class CommitDTO implements Serializable {

    private Long id;

    @NotNull
    private String hash;

    private String createdBy;

    private Instant createdDate;

    private Instant lastModifiedDate;

    private String lastModifiedBy;

    private Long issueId;

    private Long ownedById;

    private String ownedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public Long getOwnedById() {
        return ownedById;
    }

    public void setOwnedById(Long userId) {
        this.ownedById = userId;
    }

    public String getOwnedByLogin() {
        return ownedByLogin;
    }

    public void setOwnedByLogin(String userLogin) {
        this.ownedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommitDTO commitDTO = (CommitDTO) o;
        if (commitDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commitDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommitDTO{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", issue=" + getIssueId() +
            ", ownedBy=" + getOwnedById() +
            ", ownedBy='" + getOwnedByLogin() + "'" +
            "}";
    }
}
