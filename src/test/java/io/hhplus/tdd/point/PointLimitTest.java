package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PointLimitTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistory pointHistory;

    @InjectMocks
    private  PointService pointService;
    
    @Test
    @DisplayName("당일 포인트 사용이 5번만 사용가능합니다.")
    void usePointLimitTest() throws Exception{
        // given
        UserPointTable userPointTable = new UserPointTable();
        PointHistoryTable pointHistoryTable = new PointHistoryTable();
        PointService pointService = new PointService(userPointTable, pointHistoryTable);

        long userId = 1L;

        // 먼저 5번 사용
        pointService.chargePoint(userId, 100000L); // 충전
        pointService.usePoint(userId, 1000L);  // 1번
        pointService.usePoint(userId, 1000L);  // 2번
        pointService.usePoint(userId, 1000L);  // 3번
        pointService.usePoint(userId, 1000L);  // 4번
        pointService.usePoint(userId, 1000L);  // 5번

        // when & then
        assertThatThrownBy(() -> pointService.usePoint(userId, 1000L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("하루 포인트 사용 횟수(5회)를 초과했습니다");
        }
}
