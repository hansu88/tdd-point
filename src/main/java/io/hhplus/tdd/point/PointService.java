package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final UserPointTable userPointTable;

    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint getUserPoint(long userId) {

//        return UserPoint.empty(userId);
//        실제 조회 구현
        return userPointTable.selectById(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        // 유효성 검증
        if (amount < 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다");
        }
        if (amount > 1000000L) {
            throw new IllegalArgumentException("최대 충전 한도를 초과했습니다");
        }

        // 현재 포인트 조회
        UserPoint userPoint = userPointTable.selectById(userId);

        // 현재 포인트 추가 및 업데이트
        long newAmount = userPoint.point() + amount;

        return  userPointTable.insertOrUpdate(userId,newAmount);

//        return new UserPoint(userId, amount, System.currentTimeMillis());
    }

    public UserPoint usePoint(long userId, long amount) {
        // 현재 포인트 조회
        UserPoint currentUserPoint = userPointTable.selectById(userId);

        if (currentUserPoint.point() < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }

        // 포인트 차감
        long newPoint = currentUserPoint.point() - amount;

        // 포인트 저장
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userId, newPoint);

        return updatedUserPoint;
    }
}