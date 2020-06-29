package io.my.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;

public class SampleResource implements RepresentationModelProcessor<EntityModel<Sample>>{

    @Override
    public EntityModel<Sample> process(EntityModel<Sample> entityModel) {
        entityModel.add(linkTo(methodOn(SampleController.class).selectWithProcessor(new Sample())).withSelfRel());
        entityModel.add(linkTo(methodOn(SampleController.class).selectSample(new Sample())).withRel("select-sample"));
        entityModel.add(linkTo(methodOn(SampleController.class).deleteSample(new Sample())).withRel("delete-sample"));
        entityModel.add(Link.of("/docs/index.html#resources-sample-select").withRel("profile"));
        return entityModel;
    }
    
}