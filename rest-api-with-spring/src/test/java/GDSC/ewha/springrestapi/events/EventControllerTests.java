package GDSC.ewha.springrestapi.events;

import GDSC.ewha.springrestapi.common.RestDocsConfiguration;
import GDSC.ewha.springrestapi.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest // 애플리케이션을 실행했을 때와 가장 근사한 형태로 테스트 수행
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test") // "test" 라는 프로파일로 이 테스트를 실행하겠음
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
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(Matchers.not(EventStatus.DRAFT)))
//                .andExpect(jsonPath("_links.query-events").exists())
//                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        // 링크 문서화
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        // 요청 헤더 문서화
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        // 요청 필드 문서화
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("DateTime of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("DateTime of close of new event"),
                                fieldWithPath("beginEventDateTime").description("DateTime of begin of new event"),
                                fieldWithPath("endEventDateTime").description("DateTime of end of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        // 응답 헤더 문서화
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        // 응답 필드 문서화
                        responseFields(
                                fieldWithPath("id").description("Identifier of new event"),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("DateTime of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("DateTime of close of new event"),
                                fieldWithPath("beginEventDateTime").description("DateTime of begin of new event"),
                                fieldWithPath("endEventDateTime").description("DateTime of end of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("It tells if this event is free or not"),
                                fieldWithPath("offline").description("It tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("event status"),
                                // 링크 정보 기술
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event lists"),
                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
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
                .andExpect(jsonPath("errors[0].objectName").exists()) // 에러 배열에서 객체 이름
                .andExpect(jsonPath("errors[0].defaultMessage").exists()) // 기본 메시지
                .andExpect(jsonPath("errors[0].code").exists()) // 에러 코드
                .andExpect(jsonPath("_links.index").exists())
        ;
    }
}
