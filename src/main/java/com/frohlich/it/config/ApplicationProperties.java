package com.frohlich.it.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;

/**
 * Properties specific to Issue Tracker.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Repository repository = new Repository();
    private final File file = new File();

    public Repository getRepository() {
        return repository;
    }

    public File getFile(){
        return file;
    }

    public class File {
        @Nullable
        private String uploadDir;

        File () {

        }

        public String getUploadDir() {
            return uploadDir;
        }

        public void setUploadDir(String uploadDir) {
            this.uploadDir = uploadDir;
        }
    }

    public class Repository {
        @Nullable
        private String repoDir;

        Repository () {

        }

        public String getRepoDir() {
            return repoDir;
        }

        public void setRepoDir(String repoDir) {
            this.repoDir = repoDir;
        }
    }
}
