package com.game.gamelist.mapper;


import com.game.gamelist.dto.UserBasicDTO;
import com.game.gamelist.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserBasicMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "userPicture", target = "userPicture")
    @Mapping(source = "bannerPicture", target = "bannerPicture")
    UserBasicDTO userToUserBasicDTO(User user);
}
