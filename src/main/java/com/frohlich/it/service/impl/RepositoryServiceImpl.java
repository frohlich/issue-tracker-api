package com.frohlich.it.service.impl;

import com.frohlich.it.config.ApplicationProperties;
import com.frohlich.it.service.RepositoryService;
import com.frohlich.it.service.dto.ProjectDTO;
import com.google.common.base.CaseFormat;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

@Service
@Transactional
public class RepositoryServiceImpl implements RepositoryService {


    private final ApplicationProperties.Repository repository;

    private final Logger log = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    RepositoryServiceImpl(ApplicationProperties applicationProperties) {
        this.repository = applicationProperties.getRepository();
    }

    @Override
    public Repository create(ProjectDTO projectDTO) throws IOException  {
        String repoName = this.generateRepositoryName(projectDTO.getTitle());
        File localPath = new File(repository.getRepoDir() + repoName);

        Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));

        repository.create();

        repository.getConfig().setBoolean("http", null, "receivepack", true);
        repository.getConfig().save();

        return repository;
    }

    @Override
    public Repository createWithFirstCommit(ProjectDTO projectDTO) throws IOException, GitAPIException{
        final Repository repository = this.create(projectDTO);
        this.createFirstCommit(repository);
        return repository;
    }

    private String generateRepositoryName(String name) {
        return "/" + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, name.replaceAll(" ", "_").toUpperCase());
    }

    @Override
    public void createFirstCommit(Repository repo) throws IOException, GitAPIException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final String filename = "README.md";

        try (Git git = new Git(repo)) {
            final File readme = new File(classLoader.getResource("templates/commit/" + filename).getFile());
            final File readmeOnRepo = new File(repo.getWorkTree() + "/" + filename);

            log.info("Copied file " + readme.getPath() + " to  " + readmeOnRepo.getPath());

            org.apache.commons.io.FileUtils.copyFile(readme, readmeOnRepo);

            git.add()
                .addFilepattern(filename)
                .call();

            git.commit()
                .setMessage("Added README.md")
                .call();

            log.info("Committed file " + readme + " to repository at " + readmeOnRepo.getPath());
        }
    }
    /**
     * Delete the "id" project.
     *
     * @param id the id of the entity
     */

    public void delete(String localPath) throws IOException {
        final File repo = new File(localPath, ".git");
        Repository repository = FileRepositoryBuilder.create(repo);
        FileUtils.deleteDirectory(repository.getDirectory());
    }
}
