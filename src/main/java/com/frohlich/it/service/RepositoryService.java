package com.frohlich.it.service;

import com.frohlich.it.service.dto.ProjectDTO;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;

public interface RepositoryService {

    /**
     * Create an Repository.
     *
     * @param Project the entity to create repository
     * @return the persisted entity
     */
    Repository create(String name) throws IOException;

    /**
     * Create an Repository with Default commit.
     *
     * @param Project the entity to create repository
     * @return the persisted entity
     */
    Repository createWithFirstCommit(String name) throws IOException, GitAPIException;
    /**
     * Create first commit.
     *
     * @param Project the entity to create repository
     * @return void
     */
    void createFirstCommit(Repository repo) throws IOException, GitAPIException;

    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity
     */
    void delete(String localPath) throws IOException;

}
