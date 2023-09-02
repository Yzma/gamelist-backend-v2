package com.game.gamelist.service.impl;

import com.game.gamelist.entity.*;
import com.game.gamelist.exception.InvalidInputException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.projection.LikeEntityView;
import com.game.gamelist.repository.*;
import com.game.gamelist.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final InteractiveEntityRepository interactiveEntityRepository;

//    Sending ID as a parameter and check if they are instance of Post or GameJournal
    @Override
    public LikeEntityView createLike(User principle, Long interactiveEntityId) {

        // Check if the user has already liked the InteractiveEntity
        boolean alreadyLiked = likeRepository.existsByUserIdAndInteractiveEntityId(principle.getId(), interactiveEntityId);

        if (alreadyLiked) {
            throw new InvalidInputException("You have already liked this entity.");
        }

        User owner = userRepository.findById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LikeEntity like = new LikeEntity();

        Optional<InteractiveEntity> interactiveEntityOptional = interactiveEntityRepository.findById(interactiveEntityId);

        like.setUser(owner);

        if(interactiveEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("The entity is not found");
        }

        InteractiveEntity interactiveEntity = interactiveEntityOptional.get();

        if(interactiveEntity instanceof Post || interactiveEntity instanceof GameJournal || interactiveEntity instanceof Comment || interactiveEntity instanceof StatusUpdate) {
            like.setInteractiveEntity(interactiveEntity);

        } else {
            throw new RuntimeException("Invalid likeable entity");
        }

        like = likeRepository.save(like);

        return likeRepository.findProjectedById(like.getId());
    }

    @Override
    @Transactional
    public void deleteLikeById(User principle, Long interactiveEntityId) {

        boolean alreadyLiked = likeRepository.existsByUserIdAndInteractiveEntityId(principle.getId(), interactiveEntityId);

        if (!alreadyLiked) {
            throw new ResourceNotFoundException("The like on this entity does not exist.");
        }

        likeRepository.deleteByUserIdAndInteractiveEntityId(principle.getId(), interactiveEntityId);

    }
}
