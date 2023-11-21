package com.project.restapiwithspring.common;

import com.project.restapiwithspring.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.validation.Errors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {
    public static EntityModel<Errors> modelOf(Errors errors) {
        EntityModel<Errors> errorsModel = EntityModel.of(errors);
        errorsModel.add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
        return errorsModel;
    }
}
