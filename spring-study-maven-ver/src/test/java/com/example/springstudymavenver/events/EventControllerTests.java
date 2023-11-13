package com.example.springstudymavenver.events;

import org.junit.jupiter.api.Test;
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
@WebMvcTest //웹과 관련된 빈들 모두 등록, Test에서 MockMvc 주입 받아 사용 가능
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; //간단하게 요청을 만들고 응답을 검증할 수 있는 클래스

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated()); // 201 - Created
    }

}
