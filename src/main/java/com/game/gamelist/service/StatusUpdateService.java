package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.StatusUpdateView;

import java.util.List;

public interface StatusUpdateService {

    List<StatusUpdateView> findAllStatusUpdatesByUserId(User principal);

}
