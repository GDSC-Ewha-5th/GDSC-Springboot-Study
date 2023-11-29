package me.lsh.restapidemo.events;

import me.lsh.restapidemo.common.BaseControllerTest;
import me.lsh.restapidemo.common.TestDescription;
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

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON) //확장자 처럼 만들음??
                        .content(objectMapper.writeValueAsString(event))) //안에다 주는게 요청, 하고나면 응답 나옴
                .andDo(print()) //어떤 요청, 어떤 응답 콘솔에서 볼수있
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true)) //기본: false
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")

                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields( //relaxedResponseFields 사용시 문서 일부분만 확인
                                fieldWithPath("id").description("identifier of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of enrollment"),
                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of enrollment"),
                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("base price of new event"),
                                fieldWithPath("maxPrice").description("max price of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("it tells if this event is free or not"),
                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                fieldWithPath("eventStatus").description("eventStatus"),
                                //오류 해결 위해 재검사
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query event lists"),
                                fieldWithPath("_links.update-event.href").description("link to update an existing event"),
                                fieldWithPath("_links.profile.href").description("link to profile")
                        )
                ))
        ;
    }


    @Test
    @TestDescription("입력받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100) //!
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
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON) //확장자 처럼 만들음??
                        .content(objectMapper.writeValueAsString(event))) //안에다 주는게 요청, 하고나면 응답 나옴
                        .andDo(print()) //어떤 요청, 어떤 응답 콘솔에서 볼수있
                        .andExpect(status().isBadRequest());
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
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이대역")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("_links.index").exists());

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        //Given
        IntStream.range(0, 30).forEach(this::generateEvent); //메소드 레퍼런스. 람다식.

        //When
        this.mockMvc.perform(get("/api/events")
                    .param("page", "1")
                    .param("size", "10")
                    .param("sort", "name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
    }

    @Test
    @TestDescription("기존의 이벤트를 하나 조회하기")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);
        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"));
    }

    @Test
    @TestDescription("없는 이벤트는 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception {
        this.mockMvc.perform(get("/api/events/11883"))
                .andExpect(status().isNotFound());
    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        //Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String eventName = "Updated Event";
        eventDto.setName(eventName);
        //When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)) //데이터 보내기
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("update-event"));
    }

    @Test
    @TestDescription("입력값이 비어 있는 경우에 이벤트 수정 실패")
    //입력값이 잘못된다란? 로직에 값 자체가 없음 or 로직상 잘못됨
    public void updateEvent400_Empty() throws Exception {
        //Given
        Event event = this.generateEvent(200);

        EventDto eventDto = new EventDto();
        //When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception {
        //Given
        Event event = this.generateEvent(200);

        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);
        //When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class); //데이터 유효
        //When & Then
        this.mockMvc.perform(put("/api/events/123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event" + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 8, 0))
                .closeEnrollmentDateTime(LocalDateTime.of(2023, 11, 14, 22, 0))
                .beginEventDateTime(LocalDateTime.of(2023, 11, 15, 19, 0))
                .endEventDateTime(LocalDateTime.of(2023, 11, 15, 21, 0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("이대역")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }
}
