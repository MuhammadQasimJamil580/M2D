package com.M2D.EmissionAssessment;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.sql.DataSource;

@SpringBootApplication
@EnableDiscoveryClient
public class EmissionAssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmissionAssessmentApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		ModelMapper mp = new ModelMapper();
		mp.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return mp;
	}
	@Bean
	public WebClient.Builder webClient(){
		return WebClient.builder();
	}



//@Bean
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new RequestIntercepter())
//				.addPathPatterns("/**"); // Apply the interceptor to all URLs
//	}
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

}
