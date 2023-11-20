package GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.events;

//https://github.com/keesun/study/tree/master/rest-api-with-spring

import GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.common.ErrorsResource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;


@Controller
@RequestMapping(value = "api/events", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {
    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;
    @PostMapping("api/events")
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto,
                                      Errors errors){
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(new ErrorsResource(errors));
        }

        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        Event newEvent = this.eventRepository.save(event);

        WebMvcLinkBuilder  selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        URI createUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        return ResponseEntity.created(createUri).body(eventResource);
    }

}
