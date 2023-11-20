package GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Gdsc5thSpringRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(Gdsc5thSpringRestApiApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
