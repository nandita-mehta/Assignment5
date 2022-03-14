package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "com.example.demo.*")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class Assignment5Application {
	public static void main(String[] args) {
		SpringApplication.run(Assignment5Application.class, args);
	}
}
