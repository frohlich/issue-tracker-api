package com.frohlich.it.config;

import io.github.jhipster.config.JHipsterConstants;
import io.github.jhipster.config.h2.H2ConfigurationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;

@Configuration
@EnableJpaRepositories("com.frohlich.it.repository")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
@EnableElasticsearchRepositories("com.frohlich.it.repository.search")
public class DatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(DatabaseConfiguration.class);

}
