package GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.events;

import GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.events.Event;
import GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.events.EventController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


import java.util.List;

public class EventResource extends EntityModel<Event> {
    public EventResource(Event event, Link... links){
        super(event, List.of(links));
        //add(new Link("http://localhost:8080/api/events/" + event.getId()));
        //위의 코드는 Typesafe하지 않음. 아래로 바꾸어야 함.
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());
    }
}
