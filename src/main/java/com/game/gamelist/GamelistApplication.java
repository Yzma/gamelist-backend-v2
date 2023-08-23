package com.game.gamelist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.service.SeedService;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@SpringBootApplication
public class GamelistApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamelistApplication.class, args);
	}


}
