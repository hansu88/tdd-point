package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PointChargeServiceTest {

    private PointService pointService;
    private UserPointTable userPointTable;

    @BeforeEach
    void setUp() {
        UserPointTable userPointTable = new UserPointTable();  // 새로운 지역 변수! (shadowing)
        pointService = new PointService(userPointTable);
    }

    @Test
    @DisplayName("사용자에게 1000포인트를 충전하면 포인트가 1000 증가한다")
    void chargePoint_1000Amount_IncreasesPointBy1000() {
        // given
        long userId = 1L;
        long chargeAmount = 1000L;

        // when
        UserPoint result = pointService.chargePoint(userId, chargeAmount);

        // then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.point()).isEqualTo(1000L);
    }

    @Test
    @DisplayName("0 이하의 금액 충전 시 예외가 발생한다")
    void chargePoint_NegativeAmount_ThrowsException() {
        // given
        long userId = 3L;
        long invalidAmount = -100L;

        // when & then
        assertThatThrownBy(() -> pointService.chargePoint(userId, invalidAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("충전 금액은 0보다 커야 합니다");
    }

    @Test
    @DisplayName("최대 충전 한도를 초과하면 예외가 발생한다")
    void chargePoint_ExceedsMaxLimit_ThrowsException() {
        // given
        long userId = 4L;
        long maxAmount = 1_000_000L;

        // when & then
        assertThatThrownBy(() -> pointService.chargePoint(userId, maxAmount + 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("최대 충전 한도를 초과했습니다");
    }

    @Test
    @DisplayName("0원 충전 시도 시 예외가 발생한다")
    void chargePoint_ZeroReturn() throws  Exception {
        // given
        long userId = 1L;
        long zeroAmount = 0L;

        // when & then
        assertThatThrownBy(() -> pointService.chargePoint(userId, zeroAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("충전 금액은 0보다 커야 합니다");

    }
}
