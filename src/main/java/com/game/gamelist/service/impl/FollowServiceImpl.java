package com.game.gamelist.service.impl;

import com.game.gamelist.entity.User;
import com.game.gamelist.exception.InvalidInputException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.projection.FollowView;
import com.game.gamelist.projection.UserBasicView;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private static final String USER_NOT_FOUND_MSG = "User not found";
    private final UserRepository userRepository;

    @Override
    public UserBasicView createFollow(User principle, Long userId) {
        if (principle.getId().equals(userId)) {
            throw new InvalidInputException("You cannot follow yourself.");
        }

        boolean alreadyFollowed = userRepository.existsInFollowingByIdAndFollowersId(userId, principle.getId());

        if (alreadyFollowed) {
            throw new InvalidInputException("You have already followed this user.");
        }

        User user = userRepository.findWithFollowersAndFollowingById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        User userToFollow = userRepository.findWithFollowersAndFollowingById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        user.addFollowing(userToFollow);

        userRepository.save(user);

        return userRepository.findBasicViewById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }

    @Override
    public UserBasicView removeFollow(User principle, Long userId) {
        if (principle.getId().equals(userId)) {
            throw new InvalidInputException("You cannot follow or remove yourself.");
        }

        boolean alreadyFollowed = userRepository.existsInFollowingByIdAndFollowersId(userId, principle.getId());
        if (!alreadyFollowed) {
            throw new InvalidInputException("Can not found this user in your following.");
        }

        User user = userRepository.findWithFollowersAndFollowingById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        User userToUnfollow = userRepository.findWithFollowersAndFollowingById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        user.removeFollowing(userToUnfollow);
        userRepository.save(user);

        return userRepository.findBasicViewById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }

    @Override
    public UserBasicView removeFollower(User principle, Long userId) {
        if (principle.getId().equals(userId)) {
            throw new InvalidInputException("You cannot follow or remove yourself.");
        }

        boolean isFollowerExist = userRepository.existsInFollowersByIdAndFollowersId(principle.getId(), userId);

        if (!isFollowerExist) {
            throw new InvalidInputException("Can not found this user in your followers.");
        }

        User user = userRepository.findWithFollowersAndFollowingById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
        User userToRemoveInFollowers = userRepository.findWithFollowersAndFollowingById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));

        user.removeFollower(userToRemoveInFollowers);
        userRepository.save(user);

        return userRepository.findBasicViewById(userId).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }

    @Override
    public FollowView getAllFollows(User principle) {
        return userRepository.findFollowViewViewWithFollowersAndFollowingById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND_MSG));
    }
}
