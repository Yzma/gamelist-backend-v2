package com.game.gamelist.service.impl;

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
        // GameFilters are lazily loaded.
        if (cachedGameFilters == null) {
            logger.info("GameFilters are not loaded. Fetching..."); // TODO: Change this to debug
            final long startTimeInMills = System.currentTimeMillis();
            final List<String> genres = genreRepository.getAllNames();
            final List<String> platforms = platformRepository.getAllNames();
            final List<String> tags = tagRepository.getAllNames();
            final int furthestYear = gameRepository.getFurthestYear();
            this.cachedGameFilters = new GameFilters(genres, platforms, tags, furthestYear);
            logger.info("It took {}ms to fetch the game filters", System.currentTimeMillis() - startTimeInMills);
        }
        return cachedGameFilters;
    }
}
