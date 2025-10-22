package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
public class PointChargeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Test
    @DisplayName("포인트 충전 API 호출시 충전된 포인트를 반환한다")
    void chargePoint_Success() throws Exception {
        // given
        long userId = 1L;
        long chargeAmount = 5000L;
        UserPoint expectedPoint = new UserPoint(userId, 5000L, System.currentTimeMillis());

        when(pointService.chargePoint(anyLong(), anyLong()))
                .thenReturn(expectedPoint);

        // when & then
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(chargeAmount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(chargeAmount))
                .andExpect(jsonPath("$.updateMillis").exists());
    }

    @Test
    @DisplayName("음수 금액 충전 시도시 400에러를 반환한다")
    void chargePont_400Error() throws Exception {

        // given
        long userId = 1L;
        long chargeAmount = -1000L;

        when(pointService.chargePoint(anyLong(), anyLong()))
                .thenThrow(new IllegalArgumentException("충전 금액은 0보다 커야 합니다."));

        // when & then
        mockMvc.perform(patch("/point/{id}/charge", userId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(String.valueOf(chargeAmount)))
                .andExpect(status().isBadRequest())
                .andDo(print());;
    }
}
