package com.frohlich.it.service.mapper;

import com.frohlich.it.domain.*;
import com.frohlich.it.service.dto.AttachmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Attachment and its DTO AttachmentDTO.
 */
@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface AttachmentMapper extends EntityMapper<AttachmentDTO, Attachment> {

    @Mapping(source = "comment.id", target = "commentId")
    AttachmentDTO toDto(Attachment attachment);

    @Mapping(source = "commentId", target = "comment")
    Attachment toEntity(AttachmentDTO attachmentDTO);

    default Attachment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return attachment;
    }
}
