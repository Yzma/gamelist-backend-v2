package com.game.gamelist.service.impl;

import com.game.gamelist.dto.PostDTO;
import com.game.gamelist.dto.StatusUpdateDTO;
import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.StatusUpdate;
import com.game.gamelist.entity.User;
import com.game.gamelist.mapper.PostMapper;
import com.game.gamelist.mapper.StatusUpdateMapper;
import com.game.gamelist.model.PostAndStatusUpdateResponse;
import com.game.gamelist.repository.InteractiveEntityRepository;
import com.game.gamelist.service.InteractiveEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractiveEntityServiceImpl implements InteractiveEntityService {

    private final InteractiveEntityRepository interactiveEntityRepository;
    private final PostMapper postMapper;
    private final StatusUpdateMapper statusUpdateMapper;

    @Override
    public PostAndStatusUpdateResponse getPostAndStatusUpdateByUserId(User principle) {
        List<PostDTO> posts = new ArrayList<>();
        List<StatusUpdateDTO> statusUpdates = new ArrayList<>();

        List<InteractiveEntity> postsAndStatusUpdates = interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(principle.getId());

        return handleGetPostAndStatusUpdateResponse(posts, statusUpdates, postsAndStatusUpdates);
    }

    @Override
    public PostAndStatusUpdateResponse getPostAndStatusUpdateByUserIdAndStartingId(User principle, Long startingId, Integer limit) {
        List<PostDTO> posts = new ArrayList<>();
        List<StatusUpdateDTO> statusUpdates = new ArrayList<>();

        List<InteractiveEntity> postsAndStatusUpdates = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(principle.getId(), startingId, limit);

        return handleGetPostAndStatusUpdateResponse(posts, statusUpdates, postsAndStatusUpdates);
    }



    @Override
    public PostAndStatusUpdateResponse getPostAndStatusUpdateByUserIdFirstPage(User principle, Integer limit) {
        List<PostDTO> posts = new ArrayList<>();
        List<StatusUpdateDTO> statusUpdates = new ArrayList<>();

        List<InteractiveEntity> postsAndStatusUpdates = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdFirstPage(principle.getId(), limit);

        return handleGetPostAndStatusUpdateResponse(posts, statusUpdates, postsAndStatusUpdates);
    }

    private PostAndStatusUpdateResponse handleGetPostAndStatusUpdateResponse(List<PostDTO> posts, List<StatusUpdateDTO> statusUpdates, List<InteractiveEntity> postsAndStatusUpdates) {
        for (InteractiveEntity postOrStatusUpdate : postsAndStatusUpdates) {
            if(postOrStatusUpdate instanceof Post) {
                posts.add(postMapper.postToPostDTO((Post) postOrStatusUpdate));
            } else if (postOrStatusUpdate instanceof StatusUpdate){
                statusUpdates.add(statusUpdateMapper.statusUpdateToStatusUpdateDTO((StatusUpdate)postOrStatusUpdate));
            }
        }

        Long lastPostOrStatusUpdateId = postsAndStatusUpdates.get(postsAndStatusUpdates.size() - 1).getId();

        return PostAndStatusUpdateResponse.builder().statusUpdates(statusUpdates).posts(posts).lastPostOrStatusUpdateId(lastPostOrStatusUpdateId).build();
    }
}
