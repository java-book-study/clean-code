package io.my.restdocs.sample;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sample")
public class SampleController {
    
    @GetMapping("/select")
    public ResponseEntity<SampleDto> sampleSelect(
        @RequestBody SampleDto requestBody) {
        return ResponseEntity.ok(requestBody);
    }

}