package com.example.gdsc5thspringrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;

@SpringBootApplication
public class Gdsc5thSpringRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Gdsc5thSpringRestApiApplication.class, args);
	}

	//ModelMapper bean에 등록하기
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
