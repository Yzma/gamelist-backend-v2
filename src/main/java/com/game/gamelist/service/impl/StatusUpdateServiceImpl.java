package com.game.gamelist.service.impl;


import com.game.gamelist.entity.User;
import com.game.gamelist.model.StatusUpdateView;
import com.game.gamelist.repository.StatusUpdateRepository;
import com.game.gamelist.service.StatusUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusUpdateServiceImpl implements StatusUpdateService {

    private final StatusUpdateRepository statusUpdateRepository;

    @Override
    public List<StatusUpdateView> findAllStatusUpdatesByUserId(User principal) {
        return statusUpdateRepository.findAllProjectedByUserId(principal.getId());
    }
}
