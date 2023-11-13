package com.example.springstudymavenver.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE) // 이 클래스 안의 모든 핸들러는 이러한 형태의 응답을 보냄
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    //생성자가 하나만 있고, 이 생성자로 받아올 파라미터가 이미 빈으로 등록되었다면 Autowired 생략 가능
    public EventController(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){ //검증 결과를 error에 넣음

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }
        Event event = modelMapper.map(eventDto, Event.class);

        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();   //uri 만들기 - methodOn 쓰는 이유 이해안됨 -> url이 eventcontroller에 있지 않아서?
                                                                                        //@RequestMapping 후, 링크 만들 때 methodOn사용 안하고 걍 클래스로 만듦
        return ResponseEntity.created(createdUri).body(event); //created - 항상 uri가 있어야
    }
}
