package com.game.gamelist.service.impl;


import com.game.gamelist.entity.User;
import com.game.gamelist.model.StatusUpdateView;
import com.game.gamelist.service.StatusUpdateService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusUpdateServiceImpl implements StatusUpdateService {
    @Override
    public List<StatusUpdateView> findAllStatusUpdatesByUserId(User principal) {
        return null;
    }
}
