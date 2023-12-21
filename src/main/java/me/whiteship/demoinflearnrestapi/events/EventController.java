package me.whiteship.demoinflearnrestapi.events;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.hateoas.MediaTypes;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
@Controller
@RequestMapping(value = "/api/events/",produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository,ModelMapper modelMapper,EventValidator eventValidator){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }
    @PostMapping // ("/api/events/") -> 이거 때문에
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) { // 받기로 한 값들만 들어오게 됨
        // event Dto -> event 로 바꿔줌! 객체의 데이터들을 모두 mapping 해줌
        //this.eventRepository.save(event);
        // @Valid annotation 을 추가해주면 Event Dto 에서 바인딩 할 때 검증 수행 가능

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        eventValidator.validate(eventDto,errors);
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors); // ERROR BODY 응답을 RETURN  하게 했어야 함
        }

        Event event = modelMapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
        event.setId(10);
        return ResponseEntity.created(createdUri).body(event);
    }
}
