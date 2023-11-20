package GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.common;

import GDSC5thSpringRESTAPI.GDSC5thSpringRESTAPI.index.IndexController;
import org.springframework.validation.Errors;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends EntityModel<Errors> {
    public ErrorsResource(Errors content, Link...links){
        super(content, List.of(links));
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}
