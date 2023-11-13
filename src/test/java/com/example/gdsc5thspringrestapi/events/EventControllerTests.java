package com.example.gdsc5thspringrestapi.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest  //웹과 괸련된 bean 주입
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;  //mocking되어 있는 dispatcherServlet을 상대로 가짜 요청을 만들어서 응답을 확인할 수 있음. 계층별로 나누어서 웹과 관련된 Bean만 등록함. 더 빠름.

    @Test
    public void createEvent() throws Exception { //perform에 빨간줄 뜨면 alt+enter로 exception import
        mockMvc.perform(post("/api.events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON) //HAL의 스펙을 만족하는 응답을 받고싶다
                        )  //perform 안에 주는게 요청임
                .andExpect(status().isCreated());
    }

}
