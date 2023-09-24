package com.game.gamelist.mapper;

import com.game.gamelist.dto.CommentDTO;
import com.game.gamelist.dto.StatusUpdateDTO;
import com.game.gamelist.entity.Comment;
import com.game.gamelist.entity.StatusUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatusUpdateMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "userGame", target = "userGame")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "comments", target = "comments")
    StatusUpdateDTO statusUpdateToStatusUpdateDTO(StatusUpdate statusUpdate);

    @Mapping(target = "statusUpdate.comments", ignore = true)
    List<CommentDTO> commentListToCommentDTOList(List<Comment> comments);
}
