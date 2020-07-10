package io.my;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("@CsvSource 테스트")
public class CsvSourceTest {
    
    @CsvSource({
        "0, 1, 2, true",
        "hello, 1, false, true",
        "true, 1, 2, true"
    })
    @DisplayName("csvSource 테스트")
    @ParameterizedTest(name = "{index} => message=''{0}''")
    void csvSourceTest1(Object first, int second, String third, boolean fourth) {
        assertTrue(first instanceof String);
        assertEquals(1, second);
        assertNotNull(third);
        assertEquals(true, fourth);
    }

    @CsvSource({
        "0, 1, 2, true",
        "hello, 1, false, true",
        "true, 1, 2, true"
    })
    @DisplayName("csvSource 테스트(${index} 값 변경해보기")
    @ParameterizedTest
    void csvSourceTest2(Object first, int second, String third, boolean fourth) {
        assertTrue(first instanceof String);
        assertEquals(1, second);
        assertNotNull(third);
        assertEquals(true, fourth);
    }
}