package com.example.gdsc5thspringrestapi.events;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@RunWith(SpringRunner.class)
@WebMvcTest  //웹과 괸련된 bean 주입. 웹용 bean만 등록하므로 repository를 bean으로 등록해주지 않음 -> Mockbean 이용
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc;  //mocking되어 있는 dispatcherServlet을 상대로 가짜 요청을 만들어서 응답을 확인할 수 있음. 계층별로 나누어서 웹과 관련된 Bean만 등록함. 더 빠름.


    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository eventRepository; //eventRepository에 해당하는 Bean을 mock으로 만듦. mock객체라서 save하더라도 return되는 값이 null임

    @Test
    public void createEvent() throws Exception { //perform에 빨간줄 뜨면 alt+enter로 exception import
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 11, 23, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 11, 24, 14, 21))
                .beginEventDateTime(LocalDateTime.of(2018, 11, 25, 14,21))
                .endEventDateTime(LocalDateTime.of(2018, 11, 26, 14, 21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        event.setId(10);
        Mockito.when(eventRepository.save(event)).thenReturn(event); //event에 save가 호출되면 event를 리턴하라 -> Null 반환하는 문제 해결

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))) //객체를 json문자열로 바꾸고 요청 본문에 넣어줌
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists());
        //HAL의 스펙을 만족하는 응답을 받고싶다
        //perform 안에 주는게 요청임

    }

}
