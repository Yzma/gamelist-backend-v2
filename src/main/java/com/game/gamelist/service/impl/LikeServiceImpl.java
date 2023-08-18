package com.game.gamelist.service.impl;

import com.game.gamelist.entity.*;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.GameJournalRepository;
import com.game.gamelist.repository.InteractiveEntityRepository;
import com.game.gamelist.repository.LikeRepository;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    private final GameJournalRepository gameJournalRepository;

    private final InteractiveEntityRepository interactiveEntityRepository;

//    Sending Post or GameJournal as a parameter and check if they are instance of Post or GameJournal
    @Override
    public LikeEntity createLike(User principle, Long interactiveEntityId) {
        System.out.println("Liked User Name👹👹👹👹👹👹👹: " + principle.getUsername());
        LikeEntity like = new LikeEntity();

        Optional<InteractiveEntity> interactiveEntityOptional = interactiveEntityRepository.findById(interactiveEntityId);

        System.out.println("Liked User Name👹👹👹👹👹👹👹: " + like.getUser().getUsername());

        like.setUser(principle);

        System.out.println("Liked User Name👹👹👹👹👹👹👹: " + like.getUser().getUsername());
//
//        Optional<InteractiveEntity> interactiveEntityOptional = interactiveEntityRepository.findById(interactiveEntity.getId());


        if(interactiveEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("The entity is not found");
        }

        InteractiveEntity interactiveEntity = interactiveEntityOptional.get();

        if(interactiveEntity instanceof Post) {
//          find post by id or throw exception
            Post post = postRepository.findById(((Post) interactiveEntity).getId()).orElseThrow(() -> new ResourceNotFoundException("Post not found"));

            like.setInteractiveEntity(post);
            System.out.println("Liked Post text👹👹👹👹👹👹👹: " + ((Post) like.getInteractiveEntity()).getText());

        } else if (interactiveEntity instanceof GameJournal) {
//           find gameJournal by id or throw exception
            GameJournal gameJournal = gameJournalRepository.findById(((GameJournal)interactiveEntity).getId()).orElseThrow(() -> new ResourceNotFoundException("GameJournal not found"));

            like.setInteractiveEntity(gameJournal);
        } else {
            throw new RuntimeException("Invalid likeable entity");
        }

        System.out.println("Liked Post text👹👹👹👹👹👹👹: " + ((Post) like.getInteractiveEntity()).getText());

        return likeRepository.save(like);
    }

    @Override
    public void deleteLike(User principle, Long interactiveEntityId) {
//        LikeEntity existingLike = likeRepository.findByUserAndLikeable(principle, likeableEntity);
//        if (existingLike != null) {
//            likeRepository.delete(existingLike);
//        }
    }
}
