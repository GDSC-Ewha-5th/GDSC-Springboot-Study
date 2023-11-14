package me.lsh.restapidemo.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.lsh.restapidemo.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest //Mvc용Mock
@AutoConfigureMockMvc
//@WebMvcTest //Mocking 안하자
public class EventControllerTests {
    @Autowired
    MockMvc mockMvc; //가짜 요청 만들어서 보내고 응답 확인할 수 있는 Test 만들수있음.
    // Slicing Test 웹과 관련된 빈들만 테스트. dispatch sublet이란거 만들어야 함, 웹서버 띄우지x

    @Autowired
    ObjectMapper objectMapper;

    //@MockBean //목빈 //SpringBootTest 사용하면 필요없어짐
    //EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")//JUnit5는 이런 기능 Annotation 지원
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Devlopment with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 8, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 22, 0))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 15, 19, 0))
                .endEventDateTime(LocalDateTime.of(2023, 11, 15, 21, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이대역")
                .build();
        //Mockito.when(eventRepository.save(event)).thenReturn(event);
        //Mocking을 안하겠습니다~
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON) //확장자 처럼 만들음??
                        .content(objectMapper.writeValueAsString(event))) //안에다 주는게 요청, 하고나면 응답 나옴
                .andDo(print()) //어떤 요청, 어떤 응답 콘솔에서 볼수있
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(Matchers.not(false)))
                .andExpect(jsonPath("offline").value(Matchers.not(true))) //기본: false
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT))
        ;
    }


    @Test
    @TestDescription("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Devlopment with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 8, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 22, 0))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 15, 19, 0))
                .endEventDateTime(LocalDateTime.of(2023, 11, 15, 21, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이대역")
                .free(true)
                .offline(false)
                .build();
        //Mockito.when(eventRepository.save(event)).thenReturn(event);
        //Mocking을 안하겠습니다~
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON) //확장자 처럼 만들음??
                        .content(objectMapper.writeValueAsString(event))) //안에다 주는게 요청, 하고나면 응답 나옴
                        .andDo(print()) //어떤 요청, 어떤 응답 콘솔에서 볼수있
                        .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString((eventDto))))
                        .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Devlopment with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 8, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 22, 0))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 15, 19, 0))
                .endEventDateTime(LocalDateTime.of(2023, 11, 15, 21, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이대역")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString((eventDto))))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists())
                .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;

    }
}
