package io.hhplus.tdd.point;

import org.springframework.stereotype.Service;

@Service
public class PointService {

    public UserPoint getUserPoint(long userId) {
        // 최소한의 구현: 항상 0포인트 반환
        return UserPoint.empty(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        // 최소한의 구현: 항상 충전한 금액만큼 반환
        return new UserPoint(userId, amount, System.currentTimeMillis());
    }
}