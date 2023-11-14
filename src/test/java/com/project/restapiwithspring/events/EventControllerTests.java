package com.project.restapiwithspring.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.restapiwithspring.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 23, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 24, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 25, 12, 30))
                .endEventDateTime(LocalDateTime.of(2023, 11, 25, 18, 30))
                .basePrice(10000)
                .maxPrice(20000)
                .limitOfEnrollment(100)
                .location("공덕역 프론트원")
                .build();

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 23, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 24, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 25, 12, 30))
                .endEventDateTime(LocalDateTime.of(2023, 11, 25, 18, 30))
                .basePrice(10000)
                .maxPrice(20000)
                .limitOfEnrollment(100)
                .location("공덕역 프론트원")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 23, 30))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 11, 23, 59)) // 등록 종료 일자가 더 빠르다
                .beginEventDateTime(LocalDateTime.of(2023, 11, 25, 12, 30))
                .endEventDateTime(LocalDateTime.of(2023, 11, 24, 18, 30)) // 행사 종료 일자가 더 빠르다
                .basePrice(10000)
                .maxPrice(5000) // max값이 base보다 작다
                .limitOfEnrollment(100)
                .location("공덕역 프론트원")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$[0].objectName").exists())
                        .andExpect(jsonPath("$[0].defaultMessage").exists())
                        .andExpect(jsonPath("$[0].code").exists());
    }
}
