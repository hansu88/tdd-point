package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceMockTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointService pointService;

    // ===== 포인트 충전 테스트 (Mock 방식) =====

    @Test
    @DisplayName("1000원을 충전하면 포인트가 1000원 증가한다")
    void chargePoint_1000_Success() {
        // given
        long userId = 1L;
        long chargeAmount = 1000L;

        // Mock 동작 정의: 현재 포인트 0
        UserPoint currentPoint = new UserPoint(userId, 0L, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(currentPoint);

        // Mock 동작 정의: 충전 후 1000포인트
        UserPoint chargedPoint = new UserPoint(userId, 1000L, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, 1000L)).thenReturn(chargedPoint);

        // when
        UserPoint result = pointService.chargePoint(userId, chargeAmount);

        // then
        assertThat(result.point()).isEqualTo(1000L);

        // verify: 메서드 호출 검증
        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, 1000L);
    }

    @Test
    @DisplayName("2000원을 충전하면 포인트가 2000원 증가한다")
    void chargePoint_2000_Success() {
        // given
        long userId = 2L;
        long chargeAmount = 2000L;

        // Mock 동작 정의: 현재 포인트 0
        UserPoint currentPoint = new UserPoint(userId, 0L, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(currentPoint);

        // Mock 동작 정의: 충전 후 2000포인트
        UserPoint chargedPoint = new UserPoint(userId, 2000L, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, 2000L)).thenReturn(chargedPoint);

        // when
        UserPoint result = pointService.chargePoint(userId, chargeAmount);

        // then
        assertThat(result.point()).isEqualTo(2000L);

        // verify
        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, 2000L);
    }
}