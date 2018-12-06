package com.frohlich.it.config.jgit;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.frohlich.it.service.ProjectService;
import com.frohlich.it.service.dto.ProjectDTO;


public class ITUploadPackFilter implements Filter {

    @Autowired
    private ProjectService projectService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
        System.out.println("### Init" + this.projectService.toString());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("### REQUEST: " + request.toString());
        System.out.println("### RESPONSE: " + response.toString());
        HttpServletResponse resp = (HttpServletResponse) response;
        
        java.util.Optional<ProjectDTO> project = this.projectService.findOne(1001L);
        
        
        System.out.println("###############: " + project.get().getTitle());
        
        // Authorization: Basic YWRtaW46YWRtaW4=
        // Authorization: Basic admin:admin
        
        //ProjectDTO project = this.projectService.findOne();

        //if (resp != null)
        // resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        //    throw new ServletException("NOT AUTHORIZED");

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {
        System.out.println("Destroy");
    }
}
