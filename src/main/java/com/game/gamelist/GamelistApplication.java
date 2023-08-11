package com.game.gamelist;

import com.game.gamelist.service.SeedService;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GamelistApplication {

	public static void main(String[] args) {
		SpringApplication.run(GamelistApplication.class, args);

//		ConfigurableApplicationContext context = SpringApplication.run(GamelistApplication.class, args);
//
//		SeedService seedService = context.getBean(SeedService.class);
//		seedService.seedUsersIfEmpty();
	}

}
