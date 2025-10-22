package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class PointQueryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PointService pointService;

    @InjectMocks
    private PointController pointController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pointController).build();
    }


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

    @Test
    @DisplayName("GET /point/{id} - 존재하지 않는 사용자도 0포인트로 정상 응답")
    void getPoint_NonExistentUser_ReturnsZeroPoint() throws Exception {
        // given
        long userId = 999L;
        UserPoint mockUserPoint = UserPoint.empty(userId);
        when(pointService.getUserPoint(userId)).thenReturn(mockUserPoint);

        // when & then
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.point").value(0L))
                .andExpect(jsonPath("$.updateMillis").exists());
    }
}
