package com.game.gamelist.service;

import com.game.gamelist.dto.PostDTO;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.PostAndStatusUpdateResponse;

import java.util.List;
import java.util.Map;

public interface InteractiveEntityService {

    PostAndStatusUpdateResponse getPostAndStatusUpdateByUserId(User principle);
}
