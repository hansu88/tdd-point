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
        userPointTable.insertOrUpdate(3L, 0L);     // 3번 유저: 0포인트
    }

    @Test
    @DisplayName("포인트가 있는 유저(1번)를 조회하면 1000포인트를 반환한다")
    void getUserPoint_UserWithPoint_Returns1000() {
        // given
        long userId = 1L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("포인트가 0인 유저(2번)를 조회하면 0포인트를 반환한다")
    void getUserPoint_UserWithZeroPoint_Returns0() {
        // given
        long userId = 2L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
    }

    @Test
    @DisplayName("포인트가 0인 유저(3번)를 조회하면 0포인트를 반환한다")
    void getUserPoint_AnotherUserWithZeroPoint_Returns0() {
        // given
        long userId = 3L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
    }

    @Test
    @DisplayName("존재하지 않는 유저(4번)를 조회하면 0포인트를 반환한다")
    void getUserPoint_NonExistentUser_Returns0() {
        // given
        long userId = 4L;

        // when
        UserPoint result = pointService.getUserPoint(userId);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(0L);
    }
}