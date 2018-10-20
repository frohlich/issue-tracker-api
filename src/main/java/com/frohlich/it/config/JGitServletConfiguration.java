package com.frohlich.it.config;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JGitServletConfiguration {

    private final ApplicationProperties applicationProperties;

    public JGitServletConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public ServletRegistrationBean<GitServlet> servletRegistrationBean() {
        ServletRegistrationBean<GitServlet> registration = new ServletRegistrationBean<GitServlet>();
        GitServlet gs = new GitServlet();
        RepositoryResolver<HttpServletRequest> repoResolver = new CustomRepositoryResolver(this.applicationProperties.getRepository().getRepoDir());
        gs.setRepositoryResolver(repoResolver);

        registration.addUrlMappings("/git/*");
        registration.setServlet(gs);

        return registration;
    }

}
