package com.example.gdsc5thspringrestapi.common;

import com.example.gdsc5thspringrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {
    public ErrorsResource(Errors content, Link... links){
        super(content, List.of(links));
        add(linkTo(methodOn(IndexController.class).index()).withRel("index")); //index controller에 있는 method를 index로 가는 링크를 index라는 릴레이션으로 에러 리소스만들어좀.
    }

}
