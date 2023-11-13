package com.example.springstudymavenver.events;

//import org.junit.jupiter.api.Test;

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

@RunWith(SpringRunner.class)
@WebMvcTest// EventController 클래스를 대상으로 설정
// 웹과 관련된 빈들 모두 등록, Test에서 MockMvc 주입 받아 사용 가능
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc; //간단하게 요청을 만들고 응답을 검증할 수 있는 클래스

    @Autowired
    ObjectMapper objectMapper; //이미 빈으로 등록 되어있음
    // Mapping Jackson을 의존성으로 들어가 있으면 objectMapper을 빈으로 자동 등록해줌


    @MockBean //@WebMvcTest 슬라이스 테스트) 웹용 빈만 등록해주고 레파지토리를 빈으로 등록 안해주기 때문에 오류남
    EventRepository eventRepository;

    @Test
    public void createEvent() throws Exception {
        //요청 제대로 보내기
        Event event  = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))//json으로 줘야함

                .andDo(print()) //실제 콘솔에서 어떤 요청과 응답을 받았는지 확인 가능
                .andExpect(status().isCreated()) // 201 - Created
                .andExpect(jsonPath("id").exists()); //응답에 id값이 있는지 확인
    }

}
