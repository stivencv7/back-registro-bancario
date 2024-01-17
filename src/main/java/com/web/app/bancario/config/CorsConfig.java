package com.web.app.bancario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
	
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			
			
			registry.addMapping("/api/login")
					.allowedOrigins("https://thriving-entremet-b01d40.netlify.app\", \"http://localhost:3000")
					.allowedMethods("*")
					.exposedHeaders("*");
		}
		};
	}
}
