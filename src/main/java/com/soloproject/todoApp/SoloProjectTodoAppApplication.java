package com.soloproject.todoApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SoloProjectTodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoloProjectTodoAppApplication.class, args);
	}

}
