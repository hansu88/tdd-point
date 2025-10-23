package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PointController.class)
public class PointHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;

    @Test
    @DisplayName("포인트 내역 조회 API 호출 시 내역 리스트를 반환한다")
    void getPointList_SuccessReturn() throws Exception {
        // given
        long userId = 1L;
        long currentTime = System.currentTimeMillis();

        List<PointHistory> mockHistories = List.of(
                new PointHistory(1L, userId, 10_000L, TransactionType.CHARGE,currentTime),
                new PointHistory(2L, userId, 3_000L, TransactionType.USE,currentTime+1000)
        );

        when(pointService.getUserPointHistory(anyLong())).thenReturn(mockHistories);

        // when & then
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].amount").value(10_000))
                .andExpect(jsonPath("$[0].type").value("CHARGE"))
                .andExpect(jsonPath("$[1].amount").value(3_000))
                .andExpect(jsonPath("$[1].type").value("USE"));
    }
}
