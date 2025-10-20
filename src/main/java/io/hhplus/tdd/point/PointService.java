package io.hhplus.tdd.point;

import java.util.List;
import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable,PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
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
     * 포인트를 사용한다 (진짜 구현)
     */
    public UserPoint usePoint(long userId, long amount) {
        // 진짜 구현 : 현재포인트 - 사용금액
        UserPoint currentPoint = userPointTable.selectById(userId);

        // 잔고 부족 체크
        if (currentPoint.point() < amount) {
            throw new IllegalArgumentException(
                    String.format("잔고가 부족합니다. 현재포인트 : %d, 사용 요청: %d",
                            currentPoint.point(), amount)
            );
        }

        long newAmount  = currentPoint.point() - amount;
        return userPointTable.insertOrUpdate(userId, newAmount);
    }

    /**
     * 포인트 내역을 조회한다 (아직 구현 없음)
     */
    public List<PointHistory> getPointHistories(long userId) {
        // 아직 구현 없음
        return null;
    }
}