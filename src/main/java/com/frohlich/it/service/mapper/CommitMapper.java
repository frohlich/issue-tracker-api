package com.frohlich.it.service.mapper;

import com.frohlich.it.domain.*;
import com.frohlich.it.service.dto.CommitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Commit and its DTO CommitDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class, UserMapper.class})
public interface CommitMapper extends EntityMapper<CommitDTO, Commit> {

    @Mapping(source = "issue.id", target = "issueId")
    @Mapping(source = "ownedBy.id", target = "ownedById")
    @Mapping(source = "ownedBy.login", target = "ownedByLogin")
    CommitDTO toDto(Commit commit);

    @Mapping(source = "issueId", target = "issue")
    @Mapping(source = "ownedById", target = "ownedBy")
    Commit toEntity(CommitDTO commitDTO);

    default Commit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Commit commit = new Commit();
        commit.setId(id);
        return commit;
    }
}
