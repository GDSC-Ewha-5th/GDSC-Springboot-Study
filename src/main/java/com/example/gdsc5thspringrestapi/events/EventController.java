package com.example.gdsc5thspringrestapi.events;


import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; //강의 자료랑 다름! 구글링함


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) { //생성자가 하나만 있고 이 생성자로 받아올 파라미터가 빈으로 등록되어있다면 Autowired 생략 가능
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }


    @PostMapping()
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) { // 클래스 안에 있는 모든 핸들러들은 HAL_JSON이라는 contentType으로 응답을 보냄
        // 원래같았으면 eventDto -> Event 로 Builder를 사용해서 옮겼어야함 -> modelMapper를 사용하면 편리함!
//        Event event = Event.builder()
//                .name(eventDto.getName())
//                .description(eventDto.getDescription())
//                .build();

        if (errors.hasErrors()){ //@Valid에서 발생한 error를 넣어줌
            return ResponseEntity.badRequest().build();
        }
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().build();
        }

        Event event = modelMapper.map(eventDto, Event.class);  //event로 변환
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
//        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
