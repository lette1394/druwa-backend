package me.druwa.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties
public class DruwaApplication {
	public static void main(String[] args) {
		SpringApplication.run(DruwaApplication.class, args);
		log.debug("========== SERVER STARTED ==========");
	}
}
