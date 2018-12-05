package com.frohlich.it.config;

import com.frohlich.it.domain.Project;
import com.frohlich.it.service.ProjectService;
import com.frohlich.it.service.dto.ProjectDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthFilter implements Filter {

    @Autowired
    private ProjectService projectService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
        System.out.println("Init" + this.projectService.toString());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("REQUEST: " + request.toString());
        System.out.println("RESPONSE: " + response.toString());
        HttpServletResponse resp= (HttpServletResponse) response;


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
