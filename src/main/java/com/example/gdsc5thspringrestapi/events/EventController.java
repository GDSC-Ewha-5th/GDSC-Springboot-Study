package com.example.gdsc5thspringrestapi.events;


import com.example.gdsc5thspringrestapi.accounts.Account;
import com.example.gdsc5thspringrestapi.accounts.AccountAdaptor;
import com.example.gdsc5thspringrestapi.accounts.CurrentUser;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors, @CurrentUser Account currentUser) { // 클래스 안에 있는 모든 핸들러들은 HAL_JSON이라는 contentType으로 응답을 보냄
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 인증된 사용자 정보 확인 가능
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
        event.setManager(currentUser);
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
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler, @CurrentUser Account account){ //spring security로 바로 주입 받기
        //expression을 사용하면 accountadaptor가 가지고 있는 객체 중에 account라는 field에 있는 값을 꺼내서 주입함.
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //현재 인증된 사용자 정보 확인 가능
//        User principal = (User)authentication.getPrincipal(); //account service에서 return했던 user가 나옴

        //페이지와 관련된 링크 정보 넘겨주기
        Page<Event> page = this.eventRepository.findAll(pageable);
        //PagedModel<EntityModel<Event>> pagedResources = assembler.toModel(page);
        var pagedResources = assembler.toModel(page, e -> new EventResource(e));  //각각의 param을 eventResource로 변경
        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        if (account != null){ //로그인 한 사용자의 경우 추가적인 정보 제공
            pagedResources.add(linkTo(EventController.class).withRel("create-event"));
        }
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id, @CurrentUser Account currentUser){
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()){ //비어있는 경우
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile")); //프로필 링크 추가
        if (event.getManager().equals(currentUser)){ //manager인 경우에만 update-event할 수 있는 링크를 노출
            eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
        }

        return ResponseEntity.ok(eventResource);

    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto, Errors errors, @CurrentUser Account currentUser){
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
        if (!existingEvent.getManager().equals(currentUser)){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
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
