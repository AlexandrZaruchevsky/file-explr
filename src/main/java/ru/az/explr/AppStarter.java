package ru.az.explr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppStarter {

	public static void main(String[] args) {
		
//		System.getProperties().forEach((k, v) -> System.out.println(k.toString() + " ::: " + v.toString()));
//		System.getenv().entrySet().stream()
//			.forEach(e -> System.out.println(e.getKey() + " ::: " + e.getValue()));
		
		SpringApplication.run(AppStarter.class, args);
	}

}
