package io.hhplus.tdd.point;


import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PointHistoryServiceTest {

    @Mock
    private PointHistoryTable pointHistoryTable;

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("사용자 포인트 내역을 반환한다")
    void getList_SuccessReturn() throws Exception {
        // given
        long userId = 1L;
        long currentTime = System.currentTimeMillis();
        List<PointHistory> expectedHistories = Arrays.asList(
                new PointHistory(1L, userId, 10000L, TransactionType.CHARGE, currentTime),
                new PointHistory(2L, userId, 3000L, TransactionType.USE, currentTime + 1000)
        );

        when(pointHistoryTable.selectAllByUserId(eq(userId))).thenReturn(expectedHistories);

        // when
        List<PointHistory> historyList  = pointService.getUserPointHistory(userId);

        // then
        assertThat(historyList).isNotNull();
        assertThat(historyList.size()).isEqualTo(2);
        assertThat(historyList.get(0).amount()).isEqualTo(10000L);
        assertThat(historyList.get(0).type()).isEqualTo(TransactionType.CHARGE);
        assertThat(historyList.get(1).amount()).isEqualTo(3000L);
        assertThat(historyList.get(1).type()).isEqualTo(TransactionType.USE);

        verify(pointHistoryTable, times(1)).selectAllByUserId(userId);
    }

    @Test
    @DisplayName("내역이 없는 사용자 조회 시 빈리스트를 반환하다")
    void getListNoUser() throws Exception {
        // given
        long userId = 999L;
        when(pointHistoryTable.selectAllByUserId(eq(userId))).thenReturn(Collections.emptyList());

        // when
        List<PointHistory> historyList = pointService.getUserPointHistory(userId);

        //then
        assertThat(historyList).isNotNull();
        assertThat(historyList.size()).isEqualTo(0);

    }

    @Test
    @DisplayName("포인트 충전시 히스토리에 저장된다")
    void chargePoint_SuccessReturn() throws Exception {
        // given
        long userId = 1L;
        long amount = 10_000L;
        long currentTime = System.currentTimeMillis();

        UserPoint existPoint = new UserPoint(userId,5_000L , currentTime);
        UserPoint updatePoint = new UserPoint(userId,15_000L,currentTime+100);

        when(userPointTable.selectById(userId)).thenReturn(existPoint);
        when(userPointTable.insertOrUpdate(userId,15_000L)).thenReturn(updatePoint);

        // when
        pointService.chargePoint(userId,amount);

        // then
        verify(pointHistoryTable, times(1)).insert(
                eq(userId),
                eq(amount),
                eq(TransactionType.CHARGE),
                anyLong()
        );

    }

    @Test
    @DisplayName("포인트 사용시 히스토리가 저장된다")
    void usePoint_SuccessReturn() throws Exception {
        // given
        long userId = 1L;
        long amount = 3_000L;
        long currentTime = System.currentTimeMillis();

        UserPoint existPoint = new UserPoint(userId,5_000L , currentTime);
        UserPoint updatePoint = new UserPoint(userId,2_000L,currentTime+100);

        when(userPointTable.selectById(userId)).thenReturn(existPoint);
        when(userPointTable.insertOrUpdate(userId,2_000L)).thenReturn(updatePoint);

        // when
        pointService.usePoint(userId,amount);

        // then
        verify(pointHistoryTable, times(1)).insert(
                eq(userId),
                eq(amount),
                eq(TransactionType.USE),
                anyLong()
        );

    }

}
