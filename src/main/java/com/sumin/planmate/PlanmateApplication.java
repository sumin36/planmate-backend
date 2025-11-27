package com.sumin.planmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PlanmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanmateApplication.class, args);
	}
}
