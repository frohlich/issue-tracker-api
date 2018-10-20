package com.frohlich.it.config;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

public class CustomRepositoryResolver implements RepositoryResolver<HttpServletRequest> {

    private String path;

    public CustomRepositoryResolver (String path) {
            this.path = path;
    }

    @Override
    public Repository open(HttpServletRequest req, String name) throws RepositoryNotFoundException,
                    ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {

    	File repo = new File(path);

        if (repo == null || repo.isDirectory() == false) {
                        throw new RepositoryNotFoundException(repo);
                }

                File gitFile = new File(repo.getPath() + "/" + name + "/.git");

                if(!gitFile.exists() || !gitFile.isDirectory()) {
                        throw new RepositoryNotFoundException(gitFile);
                }

        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        
        try {
        	Repository repository = builder.setGitDir(gitFile)
                    .readEnvironment()
                    .findGitDir()
                    .build();
        	if(repository.getObjectDatabase().exists()) {
                System.out.println("Having repository: " + repository.getDirectory());
                repository.incrementOpen();
                return repository;
            }
        } catch (IOException e) {
                e.printStackTrace();
                throw new RepositoryNotFoundException(gitFile);
                }

        return null;
    }
}
