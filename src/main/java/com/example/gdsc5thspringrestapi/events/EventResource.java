package com.example.gdsc5thspringrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//BeanSerializer를 사용해서 objectMapper가 convert함
public class EventResource extends EntityModel<Event> {

//    @JsonUnwrapped  //wrapping을 꺼내줌
//    private Event event;
//
//    public EventResource(Event event){
//        this.event = event;
//    }
//
//    public Event getEvent(){
//        return event;
//    }
    public EventResource(Event event, Link... links){
        super(event, List.of(links));
        add(linkTo(EventController.class).slash(event.getId()).withSelfRel());  //self로 가능 링크 추가
        //add(new Link("http://localhost:8080/api/events" + event.getId()) 를 typesafe하게 표현
    }

}
