package io.my.restdocs.common;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class RestDocConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer(){
        
        return configurer -> configurer.operationPreprocessors()
                                    .withRequestDefaults(prettyPrint())
                                    .withResponseDefaults(prettyPrint())
        ;
    }
    
    // static OperationRequestPreprocessor getDocumentRequest() {
    //     return preprocessRequest(
    //                     modifyUris() // (1)
    //                             .scheme("https")
    //                             .host("docs.api.com")
    //                             .removePort(),
    //                     prettyPrint()); // (2)
    // }
}