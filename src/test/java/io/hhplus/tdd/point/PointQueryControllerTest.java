package io.hhplus.tdd.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PointQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointService pointService;


    @Test
    @DisplayName("GET /point/{id} - 사용자 포인트 조회 API 테스트")
    void getPoint_ValidUserId_ReturnsUserPoint() throws Exception {
        // given
        long userId = 1L;
        UserPoint mockUserPoint = new UserPoint(userId, 1000L, System.currentTimeMillis());
        when(pointService.getUserPoint(userId)).thenReturn(mockUserPoint);

        // when & then
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(1000L));
    }
}
