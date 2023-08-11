package com.game.gamelist.service.impl;

import com.game.gamelist.exception.InternalServerErrorException;
import com.game.gamelist.model.GameFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.GenreRepository;
import com.game.gamelist.repository.PlatformRepository;
import com.game.gamelist.repository.TagRepository;
import com.game.gamelist.service.GameFilterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameFilterServiceImpl implements GameFilterService {

    private final Logger logger = LoggerFactory.getLogger(GameFilterServiceImpl.class);
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final TagRepository tagRepository;
    private final GameRepository gameRepository;

    private GameFilters cachedGameFilters;

    @Override
    public GameFilters getGameFilters() {
        if (cachedGameFilters == null) {
            try {
                logger.debug("GameFilters are not loaded. Fetching...");
                final long startTimeInMills = System.currentTimeMillis();
                final List<String> genres = genreRepository.getAllNames();
                final List<String> platforms = platformRepository.getAllNames();
                final List<String> tags = tagRepository.getAllNames();
                final int furthestYear = gameRepository.getFurthestYear();
                this.cachedGameFilters = new GameFilters(genres, platforms, tags, furthestYear);
                logger.debug("It took {}ms to fetch the game filters", System.currentTimeMillis() - startTimeInMills);
            } catch (Exception e) {
                throw new InternalServerErrorException("Failed to fetch game filters");
            }
        }
        return cachedGameFilters;
    }
}
