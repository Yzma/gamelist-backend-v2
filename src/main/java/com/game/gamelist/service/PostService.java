package com.game.gamelist.service;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.projection.PostView;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    List<PostView> findAllPostsByUserId(User principal);

    PostView findPostById(Long requestedId, User principal);

    Post createPost(Post post, User principal);

    PostView updatePostById(Long requestedId, Post post, User principal);

    void deletePostById(Long requestedId, User principal);

    List<PostView> findAllPosts(User principal);
}
