package com.game.gamelist.service.impl;

import com.game.gamelist.entity.*;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.GameJournalRepository;
import com.game.gamelist.repository.LikeRepository;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    private final GameJournalRepository gameJournalRepository;

//    Sending Post or GameJournal as a parameter and check if they are instance of Post or GameJournal
    @Override
    public LikeEntity createLike(User principle, LikeableEntity likeableEntity) {
        LikeEntity like = new LikeEntity();

        like.setUser(principle);

        if(likeableEntity instanceof Post) {
//          find post by id or throw exception
            Post post = postRepository.findById(likeableEntity.getId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

            like.setPost(post);
        } else if (likeableEntity instanceof GameJournal) {
//           find gameJournal by id or throw exception
            GameJournal gameJournal = gameJournalRepository.findById(likeableEntity.getId()).orElseThrow(() -> new ResourceNotFoundException("GameJournal not found"));

            like.setGameJournal(gameJournal);
        } else {
            throw new RuntimeException("Invalid likeable entity");
        }

        return likeRepository.save(like);
    }

    @Override
    public void deleteLike(User principle, LikeableEntity likeableEntity) {
        LikeEntity existingLike = likeRepository.findByUserAndLikeable(principle, likeableEntity);
        if (existingLike != null) {
            likeRepository.delete(existingLike);
        }
    }
}
