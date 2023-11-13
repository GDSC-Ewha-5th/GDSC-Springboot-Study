package com.example.springstudymavenver;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringStudyMavenVerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringStudyMavenVerApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){ //공용으로 쓸 수 잇슴!
        return new ModelMapper();
    }
}
