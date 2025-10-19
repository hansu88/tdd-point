package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(PointController.class)
class PointControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Test
    @DisplayName("PATCH /point/{id}/charge - 포인트 충전 (콘솔 출력 포함)")
    void chargePoint_Controller_PrintResult() throws Exception {
        long userId = 3L;
        long amount = 3000L;

        // 서비스 Mock 정의
        UserPoint chargedPoint = new UserPoint(userId, amount, System.currentTimeMillis());
        when(pointService.chargePoint(userId, amount)).thenReturn(chargedPoint);

        // PATCH 요청 + 결과 출력
        mockMvc.perform(patch("/point/{id}/charge", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(amount)))
                .andDo(print()) // 콘솔에 요청/응답 출력
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(amount));
    }
}