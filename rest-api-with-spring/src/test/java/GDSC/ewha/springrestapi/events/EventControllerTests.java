package GDSC.ewha.springrestapi.events;

import GDSC.ewha.springrestapi.common.BaseControllerTest;
import GDSC.ewha.springrestapi.common.TestDescription;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

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
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
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

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두 번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        // Given - 이벤트 30개
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When - 두 번째 페이지 조회
        this.mockMvc.perform(get("/api/events")
                        .param("page", "1") // 1이 두 번째 페이지
                        .param("size", "10") // 한 페이지 사이즈
                        .param("sort", "name,DESC") // 이름 역순
                )
                // Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events")) // 문서화
        ;
    }

    /* 이벤트 조회 API 테스트 */
    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);// 이벤트 하나 생성

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId())) // URI path에 id 값 넣어줌
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event")) // 문서화
        ;
    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때 404 응답 받기")
    public void getEvent404() throws Exception {
        // Given - 이벤트 생성 X

        // When & Then
        this.mockMvc.perform(get("/api/events/11883")) // URI path에 id 값 넣어줌
                .andExpect(status().isNotFound()) // 404
        ;
    }

    /* 이벤트 수정 API 테스트 */
    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200); // 이벤트 하나 생성

        EventDto eventDto = this.modelMapper.map(event, EventDto.class); // 이벤트를 수정할 Dto
        String eventName = "Updated Event"; // 수정할 것만 수정
        eventDto.setName(eventName);

        // When & Then - 업데이트 요청
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName)) // 수정한 이름하고 같은지
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event")) // 문서화
        ;
    }

    /*
        [이벤트 수정 실패하는 경우]
        1. 값 자체가 없는 경우
        2. 로직상 잘못된 경우
        3. 존재하지 않는 이벤트인 경우
     */
    @Test
    @TestDescription("입력값이 비어 있는 경우에 이벤트 수정 실패") // 1. 값 자체가 없는 경우
    public void updateEvent400Empty() throws Exception {
        // Given
        Event event = this.generateEvent(200); // 이벤트 하나 생성

        EventDto eventDto = new EventDto();

        // When & Then - 업데이트 요청
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패") // 2. 로직상 잘못된 경우
    public void updateEvent400Wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);// 이벤트 하나 생성

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // 값이 잘못된 경우
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        // When & Then - 업데이트 요청
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest()) // 400
        ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패") // 3. 존재하지 않는 이벤트인 경우
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);// 이벤트 하나 생성
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then - 업데이트 요청
        this.mockMvc.perform(put("/api/events/11231", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound()) // 404
        ;
    }
    private Event generateEvent(int index) { // 이벤트 생성하기
        Event event = Event.builder()
                .name("event " + index)
                .description("test index " + index)
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 11, 11, 11))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 12, 11, 11))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 22, 11, 11))
                .endEventDateTime(LocalDateTime.of(2023, 11, 23, 11, 11))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이화여자대학교 신공학관")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

       return this.eventRepository.save(event);
    }
}
