package com.webon.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitConfiguration {

	@Autowired
	private APILayerConfiguration apiConfig;

	@Bean
	Retrofit retrofit() {
		return new Retrofit.Builder().addConverterFactory(JacksonConverterFactory.create()).baseUrl(apiConfig.getUrl()).build();
	}

	@Bean
	ValidationClientService validateClientService(Retrofit retrofit) {
		return retrofit.create(ValidationClientService.class);
	}

}
