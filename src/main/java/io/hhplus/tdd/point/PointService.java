package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    public UserPoint getUserPoint(long userId) {
        // 최소한의 구현: 항상 0포인트 반환
        return UserPoint.empty(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        // 유효성 검증
        if (amount < 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다");
        }
        if (amount > 1000000L) {
            throw new IllegalArgumentException("최대 충전 한도를 초과했습니다");
        }

        return new UserPoint(userId, amount, System.currentTimeMillis());
    }
}