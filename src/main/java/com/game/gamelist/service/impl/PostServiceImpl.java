package com.game.gamelist.service.impl;


import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidInputException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.model.LikeEntityView;
import com.game.gamelist.model.PostDto;
import com.game.gamelist.model.PostView;
import com.game.gamelist.model.UserBasicView;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public PostView updatePostById(Long requestedId, Post post, User principal) {
        if(principal == null) throw new InvalidTokenException("Invalid token");

        Optional<PostView> postOptional = postRepository.findProjectedById(requestedId, PostView.class);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post not found with ID: " + requestedId);
        }

        PostView responseData = postOptional.get();

        if(post.getText() == null || post.getText().isEmpty()) {
            throw new InvalidInputException("Text input value is invalid");
        }
        UserBasicView postOwner = responseData.getUser();

        if (!principal.getId().equals(postOwner.getId())) {
            throw new InvalidAuthorizationException("Invalid authorization");
        }

        Optional<Post> postFromDB = postRepository.findById(responseData.getId());

        Post updatedPost = postFromDB.get();
        updatedPost.setText(post.getText());
        responseData.setText(post.getText());
        updatedPost.setUpdatedAt(LocalDateTime.now());
        postRepository.save(updatedPost);

        return responseData;
    }

    @Override
    public PostView deletePostById(Long requestedId, User principal) {
        if(principal == null) throw new InvalidTokenException("Invalid token");
        Optional<Post> postOptional = postRepository.findById(requestedId);
        if (postOptional.isEmpty()) {
            throw new ResourceNotFoundException("Post not found with ID: " + requestedId);
        }

        Post responseData = postOptional.get();
        User postOwner = responseData.getUser();

        if (!principal.getId().equals(postOwner.getId())) {
            throw new InvalidAuthorizationException("Invalid authorization");
        }
        PostView postView = postRepository.findProjectedById(requestedId, PostView.class).get();

        postRepository.deleteById(requestedId);

        return postView;
    }

    @Override
    public List<Post> findAllPosts(User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");

        return postRepository.findAll();
    }

    @Override
    public Post createPost(Post post, User principal) {
        if(principal == null) throw new InvalidTokenException("Invalid token");

        if(post.getText() == null || post.getText().isEmpty()) {
            throw new InvalidInputException("Text input value is invalid");
        }
            post.setUser(principal);
            return postRepository.save(post);

    }

    @Override
    public PostView findPostById(Long requestedId, User principal) {

        if (principal == null) {throw new InvalidTokenException("Invalid token");}
        Optional<PostView> postOptional = postRepository.findProjectedById(requestedId, PostView.class);

        if (postOptional.isPresent()) {
            PostView responseData = postOptional.get();
            UserBasicView user = responseData.getUser();

            if (principal.getId().equals(user.getId())) {
                return responseData;
            }
            throw new InvalidTokenException("Invalid token");
        }

        throw new ResourceNotFoundException("Post not found with ID: " + requestedId);
    }

    @Override
    public Set<PostView> findAllPostsByUserId(User principal) {
        if (principal == null) {
            throw new InvalidTokenException("Invalid token");
        }

        Optional<Set<PostView>> optionalPosts = postRepository.findAllProjectedByUserId(principal.getId());

        if (optionalPosts.isPresent()) {
            return optionalPosts.get();
        }

        throw new ResourceNotFoundException("Posts not found for user with ID: " + principal.getId());
    }
}
