package com.frohlich.it.config.jgit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.jgit.http.server.GitServlet;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.frohlich.it.config.ApplicationProperties;
import com.frohlich.it.service.IssueService;
import com.frohlich.it.service.ProjectService;
import com.frohlich.it.service.UserService;

public class ITGitServletOverHttp extends GitServlet {

	private static final long serialVersionUID = 1L;
    
	private final ApplicationProperties applicationProperties;
    
    private final ProjectService projectService;
    
    private final IssueService issueService;

    private final UserService userService;
    
    public ITGitServletOverHttp(
    		ApplicationProperties applicationProperties,
    		ProjectService projectService,
    		IssueService issueService,
    		UserService userService) throws ServletException{
        this.applicationProperties = applicationProperties;
        this.issueService = issueService;
        this.projectService = projectService;
        this.userService = userService;
        
        this.setRepositoryResolver(
        		new ITRepositoryResolver(
        				this.applicationProperties.getRepository().getRepoDir(),
    					this.projectService, this.userService));
        //setReceivePackFactory(new WLReceivePackFactory(repoStore, bridge));
        //setUploadPackFactory(new WLUploadPackFactory());
        
    }
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, 
				config.getServletContext());
		
		this.projectService.findOne(1001L);
	}

}
