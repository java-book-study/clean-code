package io.my;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

@DisplayName("@CsvFile 테스트")
public class CsvFileTest {
    
    @CsvFileSource(resources = "/test.csv")
    @ParameterizedTest
    @DisplayName("csvSource 테스트")
    void csvSourceTest1(int first, int second, int sum) {
        assertEquals(sum, first + second);
    }
}