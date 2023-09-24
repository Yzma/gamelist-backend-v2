package com.game.gamelist.mapper;


import com.game.gamelist.dto.UserGameDTO;
import com.game.gamelist.entity.UserGame;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserGameMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "game", target = "game")
    @Mapping(source = "user", target = "user")
    UserGameDTO userGameToUserGameDTO(UserGame userGame);
}
