package com.game.gamelist.mapper;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.entity.*;
import com.game.gamelist.repository.UserGameRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface GameMapper {
    @Mapping(source = "platforms", target = "platforms", qualifiedByName = "platformSetToStringList")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "tagSetToStringList")
    @Mapping(source = "genres", target = "genres", qualifiedByName = "genreSetToStringList")
    @Mapping(source = "releaseDate", target = "releaseDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "avgScore", target = "avgScore")
    @Mapping(source = "imageURL", target = "imageURL", defaultValue = "")
    @Mapping(source = "bannerURL", target = "bannerURL", defaultValue = "")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name", defaultValue = "")
    GameDTO gameToGameDTO(Game game);

    List<GameDTO> gamesToGameDTOs(List<Game> games);

    @Named("platformSetToStringList")
    default List<String> platformSetToStringList(Set<Platform> platforms) {
        return platforms.stream()
                .map(Platform::getName)
                .toList();
    }

    @Named("tagSetToStringList")
    default List<String> tagSetToStringList(Set<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }

    @Named("genreSetToStringList")
    default List<String> genreSetToStringList(Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getName)
                .toList();
    }

}
