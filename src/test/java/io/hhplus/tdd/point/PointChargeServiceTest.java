package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointChargeServiceTest {

    private PointService pointService;

    @BeforeEach
    void setUp() {
        pointService = new PointService();
    }

    @Test
    @DisplayName("사용자에게 1000포인트를 충전하면 포인트가 1000 증가한다")
    void chargePoint_1000Amount_IncreasesPointBy1000() {
        // given
        long userId = 1L;
        long chargeAmount = 1000L;

        // when
        UserPoint result = pointService.chargePoint(userId, chargeAmount);  // 🔴 메서드 없음

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(1000L);
    }
}
