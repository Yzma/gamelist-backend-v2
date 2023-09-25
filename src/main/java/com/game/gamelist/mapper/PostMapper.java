package com.game.gamelist.mapper;

import com.game.gamelist.dto.CommentDTO;
import com.game.gamelist.dto.PostDTO;
import com.game.gamelist.entity.Comment;
import com.game.gamelist.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "text", target = "text")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "comments", target = "comments")
    PostDTO postToPostDTO(Post post);

    @Mapping(target = "post.comments", ignore = true)
    List<CommentDTO> commentListToCommentDTOList(List<Comment> comments);
}
