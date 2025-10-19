package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Test
    @DisplayName("포인트가 1000원인 유저가 500원을 충전하면 1500원이 된다")
    void chargePoint_AccumulatePoints_Success() {
        // given
        long userId = 3L;
        long initialPoint = 1000L;
        long chargeAmount = 500L;
        long expectedPoint = 1500L;

        UserPoint currentPoint = new UserPoint(userId, initialPoint, System.currentTimeMillis());
        UserPoint chargedPoint = new UserPoint(userId, expectedPoint, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(currentPoint);
        when(userPointTable.insertOrUpdate(userId, expectedPoint)).thenReturn(chargedPoint);

        // when
        UserPoint result = pointService.chargePoint(userId, chargeAmount);

        // then
        assertThat(result.point()).isEqualTo(expectedPoint);
        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, expectedPoint);

    }

    @Test
    @DisplayName("충전 시 UserPointTable의 메서드가 올바르게 호출된다")
    void chargePoint_CallsTableMethodsCorrectly() {
        // given
        long userId = 4L;
        long chargeAmount = 3000L;

        UserPoint currentPoint = new UserPoint(userId, 0L, System.currentTimeMillis());
        UserPoint chargedPoint = new UserPoint(userId, 3000L, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(currentPoint);
        when(userPointTable.insertOrUpdate(userId, 3000L)).thenReturn(chargedPoint);

        // when
        pointService.chargePoint(userId, chargeAmount);

        // then
        verify(userPointTable, times(1)).selectById(userId);  // 정확히 1번 호출
        verify(userPointTable, times(1)).insertOrUpdate(userId, 3000L);  // 정확히 1번 호출
        verifyNoMoreInteractions(userPointTable);  // 다른 메서드는 호출 안 됨
    }

    // ===== 포인트 사용 테스트 (Mock 방식) =====

    @Test
    @DisplayName("포인트 1000원이 있을 때 500원을 사용하면 500원이 남는다")
    void usePoint_HasEnoughPoint_Success() {
        // given
        long userId = 1L;
        long currentPoint = 1000L;
        long useAmount = 500L;
        long expectedPoint = 500L;

        UserPoint current = new UserPoint(userId, currentPoint, System.currentTimeMillis());
        UserPoint used = new UserPoint(userId, expectedPoint, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(current);
        when(userPointTable.insertOrUpdate(userId, expectedPoint)).thenReturn(used);

        // when
        UserPoint result = pointService.usePoint(userId, useAmount);

        // then
        assertThat(result.point()).isEqualTo(expectedPoint);
        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, expectedPoint);

    }

    @Test
    @DisplayName("포인트 2000원이 있을 때 800원을 사용하면 1200원이 남는다")
    void usePoint_DifferentAmount_Success() {
        // given
        long userId = 2L;
        long currentPoint = 2000L;
        long useAmount = 800L;
        long expectedPoint = 1200L;

        UserPoint current = new UserPoint(userId, currentPoint, System.currentTimeMillis());
        UserPoint used = new UserPoint(userId, expectedPoint, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(current);
        when(userPointTable.insertOrUpdate(userId, expectedPoint)).thenReturn(used);

        // when
        UserPoint result = pointService.usePoint(userId, useAmount);

        // then
        assertThat(result.point()).isEqualTo(expectedPoint);
        verify(userPointTable).selectById(userId);
        verify(userPointTable).insertOrUpdate(userId, expectedPoint);

    }

    @Test
    @DisplayName("포인트가 부족하면 예외가 발생한다")
    void usePoint_InsufficientAmount_ThrowsException() {
        // given
        long userId = 3L;
        long currentPoint = 500L;
        long useAmount = 1000L;

        UserPoint current = new UserPoint(userId, currentPoint, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(current);
        
        // when & then
        assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔고가 부족합니다");

        // verify: insertOrUpdate는 호출되지 않아야 함
        verify(userPointTable).selectById(userId);
        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());
    }

}