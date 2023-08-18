package com.game.gamelist.service.impl;


import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Post updatePostById(Long requestedId, Post post, User principal) {
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

        responseData.setText(post.getText());
        return postRepository.save(responseData);
    }

    @Override
    public Post deletePostById(Long requestedId, User principal) {
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

        postRepository.deleteById(requestedId);

        return responseData;
    }


    @Override
    public List<Post> findAllPosts(User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");
        List<Post> optionalPosts = postRepository.findAll();

        return optionalPosts;
    }

    @Override
    public Post createPost(Post post, User principal) {
        if(principal == null) throw new InvalidTokenException("Invalid token");

            post.setUser(principal);
            return postRepository.save(post);

    }

    @Override
    public Post findPostById(Long requestedId, User principal) {

        System.out.println("👹👹👹👹👹👹👹👹👹👹👹👹👹👹User principal username: " + principal.getEmail());
        if (principal == null) throw new InvalidTokenException("Invalid token");

        Optional<Post> postOptional = postRepository.findPostWithLikesById(requestedId);

        if (postOptional.isPresent()) {
            Post responseData = postOptional.get();
            User user = responseData.getUser();

            if (principal.getId().equals(user.getId())) {
                return postOptional.get();
            }
            throw new InvalidTokenException("Invalid token");
        }

        throw new ResourceNotFoundException("Post not found with ID: " + requestedId);
    }

    @Override
    public Set<Post> findAllPostsByUserId(User principal) {
        if (principal == null) {
            throw new InvalidTokenException("Invalid token");
        }

        Optional<Set<Post>> optionalPosts = postRepository.findAllByUserId(principal.getId());

        if (optionalPosts.isPresent()) {
            return optionalPosts.get();
        }

        throw new ResourceNotFoundException("Posts not found for user with ID: " + principal.getId());
    }
}
