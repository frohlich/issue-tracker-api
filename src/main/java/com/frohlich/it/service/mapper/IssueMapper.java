package com.frohlich.it.service.mapper;

import com.frohlich.it.domain.*;
import com.frohlich.it.service.dto.IssueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Issue and its DTO IssueDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProjectMapper.class})
public interface IssueMapper extends EntityMapper<IssueDTO, Issue> {

    @Mapping(source = "issue.id", target = "issueId")
    @Mapping(source = "closedBy.id", target = "closedById")
    @Mapping(source = "closedBy.login", target = "closedByLogin")
    @Mapping(source = "assignedTo.id", target = "assignedToId")
    @Mapping(source = "assignedTo.login", target = "assignedToLogin")
    @Mapping(source = "reportedBy.id", target = "reportedById")
    @Mapping(source = "reportedBy.login", target = "reportedByLogin")
    @Mapping(source = "project.id", target = "projectId")
    IssueDTO toDto(Issue issue);

    @Mapping(target = "commits", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(source = "issueId", target = "issue")
    @Mapping(target = "parents", ignore = true)
    @Mapping(source = "closedById", target = "closedBy")
    @Mapping(source = "assignedToId", target = "assignedTo")
    @Mapping(source = "reportedById", target = "reportedBy")
    @Mapping(source = "projectId", target = "project")
    Issue toEntity(IssueDTO issueDTO);

    default Issue fromId(Long id) {
        if (id == null) {
            return null;
        }
        Issue issue = new Issue();
        issue.setId(id);
        return issue;
    }
}
