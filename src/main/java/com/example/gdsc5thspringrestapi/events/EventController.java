package com.example.gdsc5thspringrestapi.events;


import com.example.gdsc5thspringrestapi.common.ErrorsResource;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.Optional;

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
//            return ResponseEntity.badRequest().body(new ErrorsResource(errors));  //그냥 에러가 아닌 인덱스 링크가 추가된 에러리소스 반환
            return badRequest(errors);
        }
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
//            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);  //event로 변환
        event.update();
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder setLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = setLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventResource.class).withRel("query-events")); //eventResource로 변환해서 link추가
//        eventResource.add(setLinkBuilder.withSelfRel()); eventResource안에 넣음
        eventResource.add(setLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){
        //페이지와 관련된 링크 정보 넘겨주기
        Page<Event> page = this.eventRepository.findAll(pageable);
        //PagedModel<EntityModel<Event>> pagedResources = assembler.toModel(page);
        var pagedResources = assembler.toModel(page, e -> new EventResource(e));  //각각의 param을 eventResource로 변경
        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()){ //비어있는 경우
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile")); //프로필 링크 추가
        return ResponseEntity.ok(eventResource);

    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if (optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if (errors.hasErrors()){
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
            return badRequest(errors);
        }

        Event existingEvent = optionalEvent.get();
        //dto에 있는 값으로 전부 변경
        this.modelMapper.map(eventDto, existingEvent); //src dest
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity badRequest(Errors errors){  //에러 리턴 중복 부분 함수로 빼내기
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
