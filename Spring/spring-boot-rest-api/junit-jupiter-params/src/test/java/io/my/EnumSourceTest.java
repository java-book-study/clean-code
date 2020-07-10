package io.my;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("Enum 테스트")
public class EnumSourceTest {
    
    @ParameterizedTest
    @EnumSource(Champion.class)
    @DisplayName("enum을 받는지 확인하는 성공 테스트")
    void enumInjectSuccessTest(Champion champion) {
        assertNotNull(champion);
    }

    @ParameterizedTest
    @EnumSource(value = Champion.class, names = {"Amumu"})
    @DisplayName("enum값을 확인하는 성공 테스트")
    void enumInjectSuccessTest2(Champion champion) {
        assertEquals(Champion.Amumu, champion);
    }
}