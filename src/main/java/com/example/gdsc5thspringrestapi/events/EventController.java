package com.example.gdsc5thspringrestapi.events;


import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; //강의 자료랑 다름! 구글링함


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) { //생성자가 하나만 있고 이 생성자로 받아올 파라미터가 빈으로 등록되어있다면 Autowired 생략 가능
        this.eventRepository = eventRepository;
    }


    @PostMapping()
    public ResponseEntity createEvent(@RequestBody Event event) { // 클래스 안에 있는 모든 핸들러들은 HAL_JSON이라는 contentType으로 응답을 보냄
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
