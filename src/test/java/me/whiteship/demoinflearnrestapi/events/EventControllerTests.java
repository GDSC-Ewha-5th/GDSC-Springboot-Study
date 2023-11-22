package me.whiteship.demoinflearnrestapi.events;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 단위 테스트라고 보기는 어려운 class
@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc; // slice - 요청, 응답 검증 가능 (웹서버를 띄우지 않아서 빠름)

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository;
    @Test
    public void createEvent() throws Exception {

        Event event = Event.builder()
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
                .build();
        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect((jsonPath("id").exists()));
    }
}
