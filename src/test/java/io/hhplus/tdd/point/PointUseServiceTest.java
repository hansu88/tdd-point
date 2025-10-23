package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

// 목 확장 class 활용하여 사용해보기
@ExtendWith(MockitoExtension.class)
public class PointUseServiceTest {

    @Mock
    private UserPointTable userPointTable;

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("보유 포인트로 사용시 포인트 차감된다")
    void usePoint_amountDecrease(){
        // given
        long userId = 1L;
        long initialPoint = 10000L;
        long useAmount = 3000L;
        long expectedPoint = 7000L; //예상 포인트

        when(userPointTable.selectById(userId))
                .thenReturn(new UserPoint(userId, initialPoint, System.currentTimeMillis()));

        when(userPointTable.insertOrUpdate(userId, expectedPoint))
                .thenReturn(new UserPoint(userId, expectedPoint, System.currentTimeMillis()));

        // when
        UserPoint usePoint = pointService.usePoint(userId, useAmount);

        // then
        assertThat(usePoint.point()).isEqualTo(expectedPoint);
        // then - insertOrUpdate 메서드확인
        verify(userPointTable).insertOrUpdate(userId, expectedPoint);
    }

    @Test
    @DisplayName("보유 포인트보다 많은 금액 사용시 예외 작업입니다")
    void usePointExceed_ThrowsException(){
        // given
        long userId = 1L;
        long initialPoint = 10000L;
        long useAmount = 13000L;

        when(userPointTable.selectById(userId))
            .thenReturn(new UserPoint(userId, initialPoint, System.currentTimeMillis()));

        // when & then
        assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("포인트가 부족합니다");

        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());
        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());
    }

    @Test
    @DisplayName("1000원 미만의 금액 사용시 예외 발생 작업입니다")
    void usePoint1000_ThrowsException() {
        // given
        long userId = 1L;
        long useAmount = 900L;

        // when & then
        assertThatThrownBy(() -> pointService.usePoint(userId, useAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용금액은 1000원 이상이여야 합니다");

        verify(userPointTable, never()).selectById(anyLong());
        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong());
    }
}
