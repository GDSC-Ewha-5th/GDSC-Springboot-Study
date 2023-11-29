package me.lsh.restapidemo.events;

import jakarta.validation.Valid;
import me.lsh.restapidemo.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/events", produces=MediaTypes.HAL_JSON_VALUE)
//이렇게 하면 methodOn 안써도됨
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    //생성자 주입
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator){
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        //body(event)는 된다(json으로 변환되어서 나감) 그러나 error는 못 담음
        //java BeanSerializer(serial: 객체를 json으로 변환) objectMapper 써서 변환
        //Java Bean Spec을 준수하지 않아서 변환할 수 없다 (HAR 따르지 x)

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update(); //갱신. 원래는 Service쪽에 행위를 위임해도 됨
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createdUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withRel("update-event")); // Update
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedResources = assembler.toModel(page, e -> new EventResource(e));
        pagedResources.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(errors.hasErrors()) {
            return badRequest(errors);
        }
        this.eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) { //로직상 문제인 경우
            return badRequest(errors);
        }
        Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto, existingEvent);
        Event savedEvent = this.eventRepository.save(existingEvent);

        EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }

    private ResponseEntity badRequest(Errors errors) { // ErrorsResource 지움
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
