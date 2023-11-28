package me.whiteship.demoinflearnrestapi.events;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.whiteship.demoinflearnrestapi.common.TestDescription;
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
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc; // slice - 요청, 응답 검증 가능 (웹서버를 띄우지 않아서 빠름)
    @Autowired
    ObjectMapper objectMapper;

    @Test // EventDto 를 사용하여 값을 만든 경우 잘 동작함
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
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

        // mocking ??
        // save 에 전달하게 되는 객체는 새로 만든 객체임
        // -> 따라서 적용이 안되고 null 이 return 됨
        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                // console에 보이는 내용 다 andExpect 를 통해서 검사 할 수 있음
                .andExpect(status().isCreated()) // 201상태인지 확인
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                // console에 보이는 내용 다 andExpect 를 통해서 검사 할 수 있음
                .andExpect(status().isBadRequest()) // 400 bad request
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                //.andDo(print())
                .andExpect(status().isBadRequest())
                ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                // wrong input -> annotation 으로 검증하기 어려운 경우임 (validate 를 따로 만들어야 함)
                .beginEnrollmentDateTime(LocalDateTime.of(2023,11,25,12,55))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,11,24,12,55))
                .beginEnrollmentDateTime(LocalDateTime.of(2023,11,23,12,55))
                .closeEnrollmentDateTime(LocalDateTime.of(2023,11,22,12,55))
                // -------------
                .basePrice(10000) // wrong
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("Ewha Womans University")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }
}
