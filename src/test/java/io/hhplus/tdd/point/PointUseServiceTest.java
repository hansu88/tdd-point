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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
