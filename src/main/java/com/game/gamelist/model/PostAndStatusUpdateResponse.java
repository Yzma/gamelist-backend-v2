package com.game.gamelist.model;

import com.game.gamelist.dto.PostDTO;
import com.game.gamelist.dto.StatusUpdateDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class PostAndStatusUpdateResponse {
    private List<PostDTO> posts;
    private List<StatusUpdateDTO> statusUpdates;
}
