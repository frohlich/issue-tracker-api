package com.frohlich.it.web.rest;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.frohlich.it.repository.CommitRepository;
import com.frohlich.it.repository.IssueRepository;
import com.frohlich.it.repository.ProjectRepository;
import com.frohlich.it.repository.UserRepository;

/**
 * REST controller for managing Dashboards.
 */

@RestController
@RequestMapping("/api")
public class DashboardResource {
	
	private final Logger log = LoggerFactory.getLogger(DashboardResource.class);
	
	private CommitRepository commitRepository;
	
	private IssueRepository issueRepository;
	
	private UserRepository userRepository;
	
	private ProjectRepository projectRepository;
	
	public DashboardResource(CommitRepository commitRepository, IssueRepository issueRepository, UserRepository userRepository, ProjectRepository projectRepository) {
		this.commitRepository = commitRepository;
		this.issueRepository = issueRepository;
		this.userRepository = userRepository;
		this.projectRepository = projectRepository;
	}
	
	@GetMapping("/dashboard/statistics")
	@Timed
	public HashMap<String, Long> statistics () {
		HashMap<String, Long> values = new HashMap<String, Long>();
		
		values.put("commits", this.commitRepository.countRegisters());
		values.put("users", this.userRepository.countRegisters());
		values.put("projects", this.projectRepository.countRegisters());
		values.put("issues", this.issueRepository.countRegisters());
		
		return values;
	}
}
