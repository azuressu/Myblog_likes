package com.example.post3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Post3Application {

	public static void main(String[] args) {
		SpringApplication.run(Post3Application.class, args);
	}

}
