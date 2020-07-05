package io.my.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sample", produces = MediaTypes.HAL_JSON_VALUE)
public class SampleController {

    @PostMapping("/sel")
    public ResponseEntity<EntityModel<Sample>> selectSample(Sample sampleRequest){

        EntityModel<Sample> entityModel = getResponseEntity();
        entityModel.add(linkTo(methodOn(SampleController.class).selectSample(new Sample())).withSelfRel());
        entityModel.add(linkTo(methodOn(SampleController.class).deleteSample(new Sample())).withRel("delete-sample"));
        entityModel.add(linkTo(methodOn(SampleController.class).selectWithProcessor(new Sample())).withRel("select-with-processor"));
        entityModel.add(Link.of("/docs/index.html#resources-sample-select").withRel("profile"));

        return ResponseEntity.ok(entityModel);
    }

    @PostMapping("/del")
    public ResponseEntity<EntityModel<Sample>> deleteSample(Sample sampleRequest){

        EntityModel<Sample> entityModel = getResponseEntity();
        entityModel.add(linkTo(methodOn(SampleController.class).deleteSample(new Sample())).withSelfRel());
        entityModel.add(linkTo(methodOn(SampleController.class).selectSample(new Sample())).withRel("select-sample"));
        entityModel.add(linkTo(methodOn(SampleController.class).selectWithProcessor(new Sample())).withRel("select-with-processor"));
        return ResponseEntity.ok(entityModel);
    }
    
    @PostMapping("/sel/test1")
    public ResponseEntity<EntityModel<Sample>> selectWithProcessor(Sample sampleRequest) {
        EntityModel<Sample> entityModel = getResponseEntityWithSampleResource();
        return ResponseEntity.ok(entityModel);
    }

    private EntityModel<Sample> getResponseEntity(){
        Sample sample = new Sample(1, "name", "Seoul, South Korea", 27);
        EntityModel<Sample> entityModel = EntityModel.of(sample);
        return entityModel;
    }

    private EntityModel<Sample> getResponseEntityWithSampleResource(){
        Sample sample = new Sample(1, "name", "Seoul, South Korea", 27);
        SampleResource sampleResource = new SampleResource();
        return sampleResource.process(EntityModel.of(sample));
    }

}