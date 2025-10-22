package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointQueryServiceTest {

    private PointService pointService;

    @BeforeEach
    void setUp() {
        pointService = new PointService();
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
}