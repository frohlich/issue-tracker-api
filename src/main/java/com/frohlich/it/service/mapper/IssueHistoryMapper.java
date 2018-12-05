package com.frohlich.it.service.mapper;

import com.frohlich.it.domain.*;
import com.frohlich.it.service.dto.IssueHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IssueHistory and its DTO IssueHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class, CommentMapper.class})
public interface IssueHistoryMapper extends EntityMapper<IssueHistoryDTO, IssueHistory> {

    @Mapping(source = "issue.id", target = "issueId")
    @Mapping(source = "comment.id", target = "commentId")
    @Mapping(source = "comment.comment", target = "commentDescription")
    IssueHistoryDTO toDto(IssueHistory issueHistory);

    @Mapping(source = "issueId", target = "issue")
    @Mapping(source = "commentId", target = "comment")
    IssueHistory toEntity(IssueHistoryDTO issueHistoryDTO);

    default IssueHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        IssueHistory issueHistory = new IssueHistory();
        issueHistory.setId(id);
        return issueHistory;
    }
}
