package me.whiteship.demoinflearnrestapi.events;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 단위 테스트라고 보기는 어려운 class
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // MockMvc 를 사용하기 위해서
@EnableWebMvc
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc; // slice - 요청, 응답 검증 가능 (웹서버를 띄우지 않아서 빠름)
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEvent() throws Exception {
        // event Dto 사용하면 값들이 잘 들어오고
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023,11,22,12,55))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,11,23,12,55))
                .beginEnrollmentDateTime(LocalDateTime.of(2023,11,24,12,55))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,11,25,12,55))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("Ewha Womans University")
                .free(true)
                .offline(false)
                .build();

        // mocking ??
        // save 에 전달하게 되는 객체는 새로 만든 객체임
        // -> 따라서 적용이 안되고 null 이 return 됨

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                        .andDo(print())
                        // console에 보이는 내용 다 andExpect 를 통해서 검사 할 수 있음
                        .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
        ;
    }
}
