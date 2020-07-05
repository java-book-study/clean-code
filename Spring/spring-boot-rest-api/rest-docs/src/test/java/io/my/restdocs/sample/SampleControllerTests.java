package io.my.restdocs.sample;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.snippet.Attributes.Attribute;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import io.my.restdocs.common.RestDocConfiguration;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(SampleController.class)
@Import(RestDocConfiguration.class)
public class SampleControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void makeRestDocs() throws Exception {

        // Given
        SampleBase sample = new SampleBase("name", "nickName", 27, 99);
        SampleDto sampleDto = new SampleDto(sample);
        String requestBody = objectMapper.writeValueAsString(sampleDto);

        // When
        ResultActions result = this.mockMvc.perform(
            get("/api/sample/select")
            .characterEncoding("utf-8")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestBody)
            )
        ;

        // Then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("sample.name").exists())
                .andExpect(jsonPath("sample.nickName").exists())
                .andExpect(jsonPath("sample.age").exists())
                .andExpect(jsonPath("sample.favoriteNumber").exists())
                .andDo(document("sample-select",
                    requestHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                    requestFields(
                        fieldWithPath("sample.name").type(JsonFieldType.STRING).description("이름").attributes(key("author").value("author")),
                        fieldWithPath("sample.nickName").optional().type(JsonFieldType.STRING).description("별명"),
                        fieldWithPath("sample.age").type(JsonFieldType.NUMBER).description("나이"),
                        fieldWithPath("sample.favoriteNumber").optional().type(JsonFieldType.NUMBER).description("가장 좋아하는 숫자")
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                    ),
                    responseFields(
                        fieldWithPath("sample.name").optional().attributes(getAttribute("author")).description("이름"),
                        fieldWithPath("sample.nickName").optional().attributes(getAttribute("author")).description("별명"),
                        fieldWithPath("sample.age").optional().attributes(getAttribute("author")).description("나이"),
                        fieldWithPath("sample.favoriteNumber").attributes(getAttribute("author")).description("가장 좋아하는 숫자")
                    )
                ))
        ;
    }
    
    private Attribute getAttribute(String author) {
        return new Attribute("author", author);
    }

}