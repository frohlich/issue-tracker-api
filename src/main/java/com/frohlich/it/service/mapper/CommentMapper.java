package com.frohlich.it.service.mapper;

import com.frohlich.it.domain.*;
import com.frohlich.it.service.dto.CommentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Comment and its DTO CommentDTO.
 */
@Mapper(componentModel = "spring", uses = {IssueMapper.class})
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {

    @Mapping(source = "issue.id", target = "issueId")
    CommentDTO toDto(Comment comment);

    @Mapping(source = "issueId", target = "issue")
    @Mapping(target = "attachments", ignore = true)
    Comment toEntity(CommentDTO commentDTO);

    default Comment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(id);
        return comment;
    }
}
