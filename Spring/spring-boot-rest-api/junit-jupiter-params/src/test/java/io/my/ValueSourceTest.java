package io.my;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("@ValueSource 테스트")
public class ValueSourceTest {

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "World"})
    @DisplayName("파라미터가 들어오는지 확인하기 위한 테스트")
    void paramNotNullTest1(String message) {
        assertNotNull(message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello"})
    @DisplayName("입력한 파라미터값이 맞는지 테스트")
    void paramTest(String message) {
        assertEquals(message, "Hello");
    }

    
}