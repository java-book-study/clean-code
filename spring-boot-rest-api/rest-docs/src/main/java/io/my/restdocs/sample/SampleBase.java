package io.my.restdocs.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleBase {
    
    private String name;
    private String nickName;
    private Integer age;
    private Integer favoriteNumber;
}