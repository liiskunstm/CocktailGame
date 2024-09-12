package com.ridango.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@RequiredArgsConstructor
@SpringBootApplication
@Getter
@Setter
public class CocktailGameApplication implements CommandLineRunner {

	private final GameService gameService;

	public static void main(String[] args) {
		SpringApplication.run(CocktailGameApplication.class, args);
	}

	@Override
	public void run(String... args) throws IOException {
		gameService.startGame();
		System.exit(0);
	}
}
