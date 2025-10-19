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
     * 포인트를 충전한다 (아직 구현 없음)
     */
    public UserPoint chargePoint(long userId, long amount) {
        // 아직 구현 없음
        return null;
    }
}