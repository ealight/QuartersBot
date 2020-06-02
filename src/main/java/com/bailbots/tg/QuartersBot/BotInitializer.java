package com.bailbots.tg.QuartersBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class BotInitializer {

	public static void main(String[] args) {
		ApiContextInitializer.init();

		SpringApplication.run(BotInitializer.class, args);
	}

}
