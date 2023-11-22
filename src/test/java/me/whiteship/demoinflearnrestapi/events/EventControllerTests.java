package me.whiteship.demoinflearnrestapi.events;
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

// 단위 테스트라고 보기는 어려운 class
@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc; // slice - 요청, 응답 검증 가능 (웹서버를 띄우지 않아서 빠름)

    @Test
    public void createEvent() throws Exception {
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated());
    }
}
