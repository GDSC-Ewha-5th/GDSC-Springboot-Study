package me.lsh.restapidemo.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public PasswordEncoder passwordEncoder() { //패스워드 앞에 prefix를 붙여 어떤 인코딩인지 확인하는 인코더
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
