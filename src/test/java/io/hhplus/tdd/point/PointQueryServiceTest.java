package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointQueryServiceTest {

    private PointService pointService;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp() {
        userPointTable = new UserPointTable();
        pointService = new PointService(userPointTable);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 포인트를 조회하면 0포인트를 반환한다")
    void getUserPoint_NonExistentUser_ReturnsZeroPoint() {
        // given
        long userId = 1L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
        assertThat(result.updateMillis()).isGreaterThan(0L);
    }


    @Test
    @DisplayName("포인트가 있는 사용자의 포인트를 조회하면 실제 포인트를 반환한다")
    void getUserPoint_ExistingUser_ReturnsActualPoint() {
        // given
        long userId = 2L;
        // TODO: 사용자에게 포인트를 먼저 설정하는 로직 필요

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        // 현재는 항상 0이지만, 나중에 실제 포인트 확인으로 변경 예정
        assertThat(result.point()).isEqualTo(0L);
    }

    @Test
    @DisplayName("사용자 포인트 조회한다")
    void getUserPoint_select() {
        // given
        long userId = 1L;
        long expectedPoint = 1L;

        pointService.chargePoint(userId,expectedPoint);

        // when
        UserPoint userpoint = pointService.getUserPoint(userId);

        // then
        assertThat(userpoint.id()).isEqualTo(userId);
        assertThat(userpoint.point()).isEqualTo(expectedPoint);
    }
}