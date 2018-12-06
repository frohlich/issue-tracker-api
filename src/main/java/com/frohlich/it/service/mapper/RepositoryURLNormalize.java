package com.frohlich.it.service.mapper;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.frohlich.it.config.ApplicationProperties;

@Component
public class RepositoryURLNormalize {

    private final ApplicationProperties applicationProperties;
    
    public RepositoryURLNormalize(ApplicationProperties applicationProperties) {
    	this.applicationProperties = applicationProperties;
    }
    
    @Named("repositoryURLNormalize")
    public String stringToEventTime(String repoName) {
        return applicationProperties.getRepository().getBaseUrl() + repoName;
    }
	
}
