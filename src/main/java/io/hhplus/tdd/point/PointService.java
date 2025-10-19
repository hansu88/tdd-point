package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;

    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    /**
     * 특정 유저의 포인트를 조회작업한다
     */
    public UserPoint getUserPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    /**
     * 포인트를 충전한다
     */
    public UserPoint chargePoint(long userId, long amount) {
        UserPoint currentPoint = userPointTable.selectById(userId);
        long newAmount  = currentPoint.point() + amount;
        return userPointTable.insertOrUpdate(userId, newAmount);
    }

    /**
     * 포인트를 사용한다 (가짜 구현)
     */
    public UserPoint usePoint(long userId, long amount) {
        // 가짜 구현 : 무조건 500원 !
        UserPoint currentPoint = userPointTable.selectById(userId);
        return userPointTable.insertOrUpdate(userId, 500L);
    }
}