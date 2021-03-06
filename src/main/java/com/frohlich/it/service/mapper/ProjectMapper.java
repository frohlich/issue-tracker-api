package com.frohlich.it.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.frohlich.it.domain.Project;
import com.frohlich.it.service.dto.ProjectDTO;

/**
 * Mapper for the entity Project and its DTO ProjectDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, RepositoryURLNormalize.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

    @Mapping(source = "ownedBy.id", target = "ownedById")
    @Mapping(source = "ownedBy.login", target = "ownedByLogin")
    @Mapping(source = "repository", target = "repository", qualifiedByName = "repositoryURLNormalize")
    ProjectDTO toDto(Project project);

    @Mapping(target = "issues", ignore = true)
    @Mapping(source = "ownedById", target = "ownedBy")
    Project toEntity(ProjectDTO projectDTO);

    default Project fromId(Long id) {
        if (id == null) {
            return null;
        }
        Project project = new Project();
        project.setId(id);
        return project;
    }
}
