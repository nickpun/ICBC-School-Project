package apiServer;

import database.DatabaseConnectorPool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedOrigins("http://localhost:3000",
								"https://localhost:3000",
								"http://pandemicpal.herokuapp.com",
								"https://pandemicpal.herokuapp.com");
			}
		};
	}
}

//https://www.baeldung.com/spring-cors
//https://www.baeldung.com/spring-security-cors-preflight

//https://stackoverflow.com/questions/51720552/enabling-cors-globally-in-spring-boot/51721298

//https://www.youtube.com/watch?v=PovQ6aUeRgg