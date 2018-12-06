package com.frohlich.it.config.jgit;

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

import com.frohlich.it.service.ProjectService;
import com.frohlich.it.service.UserService;

public class ITRepositoryResolver implements RepositoryResolver<HttpServletRequest> {

    private String path;
    
    private final ProjectService projectService;
    
    private final UserService userService;

    public ITRepositoryResolver (String path, ProjectService projectService, UserService userService) {
        this.path = path;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public Repository open(HttpServletRequest req, String name) throws RepositoryNotFoundException,
                    ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {

    	/*String username = RemoteUserUtil.getRemoteUser(req, "Authorization");
    	
    	if (username == null) {
    		username = RemoteUserUtil.getRemoteUser(req, "WWW-Authenticate");
    	}
    	
    	if (username == null) {
    		throw new ServiceNotAuthorizedException("Sem Headers de autorização");
    	}
    	
    	*/
    	
    	// Optinal user this.userService.getUserWithAuthoritiesByLogin(username);
    	
    	if (this.projectService.findByRepositoryName(name) == null) {
    		throw new RepositoryNotFoundException("Repositório não existe");
		}
    	
    	File repo = new File(path);

        if (repo == null || repo.isDirectory() == false) {
                throw new RepositoryNotFoundException(repo);
        }

        File gitFile = new File(repo.getPath() + "/" + name + "/.git");

        if (!gitFile.exists() || !gitFile.isDirectory()) {
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
