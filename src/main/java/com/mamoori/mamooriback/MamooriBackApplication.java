package com.mamoori.mamooriback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MamooriBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MamooriBackApplication.class, args);
	}

}
