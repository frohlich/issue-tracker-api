package com.frohlich.it.service.impl;

import com.frohlich.it.config.ApplicationProperties;
import com.frohlich.it.service.RepositoryService;
import com.frohlich.it.service.dto.ProjectDTO;
import com.frohlich.it.service.impl.errors.FileStorageException;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
@Transactional
public class RepositoryServiceImpl implements RepositoryService {


    private Path repositoryDir;

    private final Logger log = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    RepositoryServiceImpl(ApplicationProperties applicationProperties) {
        this.repositoryDir = Paths.get(applicationProperties.getRepository().getRepoDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.repositoryDir);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the repositories files will be stored.", ex);
        }
    }

    @Override
    public Repository create(String name) throws IOException  {
        String repoName = this.generateRepositoryName(name);
        File localPath = new File(repositoryDir + repoName);

        Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));

        repository.create(true);

        repository.getConfig().setBoolean("http", null, "receivepack", true);
        repository.getConfig().save();

        return repository;
    }

    @Override
    public Repository createWithFirstCommit(String name) throws IOException, GitAPIException{
        final Repository repository = this.create(name);
        this.createFirstCommit(repository);
        return repository;
    }

    private String generateRepositoryName(String name) {
        name = name.replaceAll("[^a-zA-Z0-9]+","");
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
     * @param localPath the local path of the entity
     */

    public void delete(String localPath) throws IOException {
        final File repo = new File(localPath, ".git");
        Repository repository = FileRepositoryBuilder.create(repo);
        FileUtils.deleteDirectory(repository.getDirectory());
    }
}
