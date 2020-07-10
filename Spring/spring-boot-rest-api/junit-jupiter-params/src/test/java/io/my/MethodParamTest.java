package io.my;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("@메서드를 이용한 파라미터 주입 테스트")
public class MethodParamTest {
    
    @ParameterizedTest
    @MethodSource("sumProvider")
    @DisplayName("메서드를 이용한 파라미터 주입 테스트")
    void sum(int a, int b, int sum) {
        assertEquals(sum, a + b);
    }
 
    private static Stream<Arguments> sumProvider() {
        return Stream.of(
                Arguments.of(1, 1, 2),
                Arguments.of(2, 3, 5)
        );
    }

    @MethodSource
    @ParameterizedTest
    @DisplayName("메서드를 이용한 파라미터 주입 테스트")
    public void sum2MethodSource(int a, int b, int sum) {
        System.out.println(a + b + " = " + sum);
        assertEquals(sum, a + b);
    }

    private static Object[] sum2MethodSource() {
        return new Object[]{
            new Object[]{1, 1, 2},
            new Object[]{2, 3, 5}
        };
    }
}