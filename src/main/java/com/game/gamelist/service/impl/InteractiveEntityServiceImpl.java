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

        for (InteractiveEntity postOrStatusUpdate : postsAndStatusUpdates) {
            if(postOrStatusUpdate instanceof Post) {
                posts.add(postMapper.postToPostDTO((Post) postOrStatusUpdate));
            } else if (postOrStatusUpdate instanceof StatusUpdate){
                statusUpdates.add(statusUpdateMapper.statusUpdateToStatusUpdateDTO((StatusUpdate)postOrStatusUpdate));
            }
        }

        PostAndStatusUpdateResponse response = PostAndStatusUpdateResponse.builder().statusUpdates(statusUpdates).posts(posts).build();

        return response;
    }
}
