package GDSC.ewha.springrestapi.events;

import GDSC.ewha.springrestapi.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest // 애플리케이션을 실행했을 때와 가장 근사한 형태로 테스트 수행
@AutoConfigureMockMvc
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /* EventDto를 사용하여 입력 값 제한 */
    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 12, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 22, 11, 11))
                .endEventDateTime(LocalDateTime.of(2023, 11, 23, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이화여자대학교 신공학관")
                .build();

        mockMvc.perform(post("/api/events/") // HTTP POST 요청 보내면
                        .contentType(MediaType.APPLICATION_JSON) // 요청의 본문에 JSON을 담아서 보내고 있음
                        .accept(MediaTypes.HAL_JSON) // 원하는 응답
                        .content(objectMapper.writeValueAsString(event))) // 위에서 작성한 event를 JSON으로 바꾸고 요청 본문에 넣기
                .andDo(print()) // 실제 응답 확인
                .andExpect(status().isCreated()) // isCreated(201) 응답
                .andExpect(jsonPath("id").exists()) // id가 있는지 확인
                .andExpect(header().exists(HttpHeaders.LOCATION)) // Location 헤더가 있는지 확인
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,  MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT)))
        ;
    }

    /* 입력 값 이외에 에러 발생 */
    @Test
    @TestDescription("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100) // unknown property
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 12, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 22, 11, 11))
                .endEventDateTime(LocalDateTime.of(2023, 11, 23, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이화여자대학교 신공학관")
                .free(true) // unknown property
                .offline(false) // unknown property
                .eventStatus(EventStatus.PUBLISHED) // unknown property
                .build();

        mockMvc.perform(post("/api/events/") // HTTP POST 요청 보내면
                        .contentType(MediaType.APPLICATION_JSON) // 요청의 본문에 JSON을 담아서 보내고 있음
                        .accept(MediaTypes.HAL_JSON) // 원하는 응답
                        .content(objectMapper.writeValueAsString(event))) // 위에서 작성한 event를 JSON으로 바꾸고 요청 본문에 넣기
                .andDo(print()) // 실제 응답 확인
                .andExpect(status().isBadRequest()) // BadRequest 응답(400)!
        ;
    }

    /* 입력 값이 안 들어오는 경우 */
    @Test
    @TestDescription("입력 값이 비어 있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception{
        EventDto eventDto = EventDto.builder().build(); // 값 없이 보냄

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());
    }

    /* 입력 값이 이상한 경우 */
    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception{
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 12, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 24, 11, 11))
                .endEventDateTime(LocalDateTime.of(2023, 11, 23, 11, 11))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이화여자대학교 신공학관")
                .build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                // 응답에 있기를 바라는 내용
                .andExpect(jsonPath("$[0].objectName").exists()) // 에러 배열에서 객체 이름
                .andExpect(jsonPath("$[0].defaultMessage").exists()) // 기본 메시지
                .andExpect(jsonPath("$[0].code").exists()) // 에러 코드
        ;
    }
}
