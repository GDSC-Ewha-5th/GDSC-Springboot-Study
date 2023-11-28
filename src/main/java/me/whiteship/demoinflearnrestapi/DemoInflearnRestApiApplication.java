package me.whiteship.demoinflearnrestapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
public class DemoInflearnRestApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoInflearnRestApiApplication.class, args);
    }

    @Bean // 공용으로 사용할 수 있기 때문에
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
