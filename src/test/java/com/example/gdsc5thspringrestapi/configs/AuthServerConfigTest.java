package com.example.gdsc5thspringrestapi.configs;

import com.example.gdsc5thspringrestapi.accounts.Account;
import com.example.gdsc5thspringrestapi.accounts.AccountRole;
import com.example.gdsc5thspringrestapi.accounts.AccountService;
import com.example.gdsc5thspringrestapi.common.AppProperties;
import com.example.gdsc5thspringrestapi.common.BaseControllerTest;
import com.example.gdsc5thspringrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {
    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;
    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception{
        //given
//기본 유저는 이미 저장되어 있으므로
//        Account keesun = Account.builder()
//                .email(appProperties.getUserUsername())
//                .password(appProperties.getUserPassword())
//                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//                .build();
//        this.accountService.saveAccount(keesun);


        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret())
                        .param("username", appProperties.getUserUsername())
                        .param("password", appProperties.getUserPassword())
                        .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

    }

}