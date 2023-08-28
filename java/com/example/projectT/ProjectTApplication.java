package com.example.projectT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
//@EnableJpaAuditing
public class ProjectTApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTApplication.class, args);
	}

}
