package com.frohlich.it.config;

import javax.servlet.ServletException;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.frohlich.it.config.jgit.ITGitServletOverHttp;
import com.frohlich.it.service.IssueService;
import com.frohlich.it.service.ProjectService;
import com.frohlich.it.service.UserService;

@Configuration
public class JGitServletConfiguration {

    private final ApplicationProperties applicationProperties;
    
    private final ProjectService projectService;
    
    private final IssueService issueService;

    private final UserService userService;
    
    public JGitServletConfiguration(
    		ApplicationProperties applicationProperties,
    		ProjectService projectService,
    		IssueService issueService,
    		UserService userService) {
        this.applicationProperties = applicationProperties;
        this.issueService = issueService;
        this.projectService = projectService;
        this.userService = userService;
    }

    /*@Bean
    public ServletRegistrationBean<GitServlet> servletRegistrationBean() {
        ServletRegistrationBean<GitServlet> registration = new ServletRegistrationBean<GitServlet>();

        GitServlet gs = new GitServlet();

        RepositoryResolver<HttpServletRequest> repoResolver = 
        		new ITRepositoryResolver(this.applicationProperties.getRepository().getRepoDir());
        gs.setRepositoryResolver(repoResolver);

        gs.addUploadPackFilter(new ITUploadPackFilter());
        gs.addReceivePackFilter(new ITReceivePackFilter());

        registration.addUrlMappings("/git/*");
        registration.setServlet(gs);

        return registration;
    }*/
    @Bean
    public ServletRegistrationBean<ITGitServletOverHttp> servletRegistrationBean() {
        ServletRegistrationBean<ITGitServletOverHttp> registration = new ServletRegistrationBean<ITGitServletOverHttp>();

        ITGitServletOverHttp gs;
		try {
			gs = new ITGitServletOverHttp(applicationProperties, 
					projectService, issueService, userService);
	        registration.addUrlMappings("/git/*");
	        registration.setServlet(gs);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        return registration;
    }
}


