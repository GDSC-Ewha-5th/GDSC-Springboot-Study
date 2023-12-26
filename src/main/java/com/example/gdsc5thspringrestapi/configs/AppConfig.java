package com.example.gdsc5thspringrestapi.configs;

import com.example.gdsc5thspringrestapi.accounts.Account;
import com.example.gdsc5thspringrestapi.accounts.AccountRole;
import com.example.gdsc5thspringrestapi.accounts.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {

    //ModelMapper bean에 등록하기
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ApplicationRunner applicationRunner(){
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;
            @Override
            public void run(ApplicationArguments args) throws Exception {
                 Account keesun = Account.builder()
                        .email("keesun@email.com")
                        .password("keesun")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                accountService.saveAccount(keesun);
            }
        }
    }
}
