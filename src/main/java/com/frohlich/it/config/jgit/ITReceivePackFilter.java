package com.frohlich.it.config.jgit;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jgit.http.server.GitSmartHttpTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.frohlich.it.service.ProjectService;
import com.frohlich.it.service.dto.ProjectDTO;

public class ITReceivePackFilter implements Filter {

    @Autowired
    private ProjectService projectService;
	
	@Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
        System.out.println("@@@ Init" + this.projectService.toString());
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
        System.out.println("@@@ REQUEST: " + request.toString());
        System.out.println("@@@ RESPONSE: " + response.toString());
        
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest  req = (HttpServletRequest) request;
        
        System.out.println("@@@@@@@@@@:" + 
        		req.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
        
        java.util.Optional<ProjectDTO> project = this.projectService.findOne(1001L);
        
        
        System.out.println("@@@@@@@@@@: " + project.get().getTitle());
        
        
        GitSmartHttpTools.sendError(
                (HttpServletRequest) request,
                (HttpServletResponse) response,
                HttpServletResponse.SC_FORBIDDEN,
                "receive-pack not permitted on this server");
            return;
            // chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
