package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.PostAndStatusUpdateResponse;



public interface InteractiveEntityService {

    PostAndStatusUpdateResponse getPostAndStatusUpdateByUserId(User principle);
}
