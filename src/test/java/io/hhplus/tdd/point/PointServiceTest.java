package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointServiceTest {

    private PointService pointService;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp() {
        userPointTable = new UserPointTable();
        pointService = new PointService(userPointTable);

        // 테스트 데이터 셋업
        userPointTable.insertOrUpdate(1L, 1000L);  // 1번 유저: 1000포인트
        userPointTable.insertOrUpdate(2L, 0L);     // 2번 유저: 0포인트
    }

    @Test
    @DisplayName("포인트가 있는 유저를 조회하면 실제 포인트를 반환한다")
    void getUserPoint_ExistingUser_ReturnsActualPoint() {
        // given
        long userId = 1L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("포인트가 0인 유저를 조회하면 0포인트를 반환한다")
    void getUserPoint_ZeroPointUser_ReturnsZero() {
        // given
        long userId = 2L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
    }

    @Test
    @DisplayName("존재하지 않는 유저를 조회하면 0포인트를 반환한다")
    void getUserPoint_NonExistentUser_ReturnsZero() {
        // given
        long userId = 999L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
    }
}