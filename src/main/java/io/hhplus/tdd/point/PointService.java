package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = new PointHistoryTable();  // 여기서 직접 생성
    }

    // 새 생성자 (Spring과 새 테스트를 위해)
    @Autowired
    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }


    public UserPoint getUserPoint(long userId) {

//        return UserPoint.empty(userId);
//        실제 조회 구현
        return userPointTable.selectById(userId);
    }

    public UserPoint chargePoint(long userId, long amount) {
        // 유효성 검증
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다");
        }
        if (amount > 1000000L) {
            throw new IllegalArgumentException("최대 충전 한도를 초과했습니다");
        }

        // 현재 포인트 조회
        UserPoint userPoint = userPointTable.selectById(userId);

        // 현재 포인트 추가 및 업데이트
        long newAmount = userPoint.point() + amount;
        UserPoint updatedPoint = userPointTable.insertOrUpdate(userId, newAmount);
        pointHistoryTable.insert(userId, amount, TransactionType.CHARGE,updatedPoint.updateMillis());

        return updatedPoint;

//        return  userPointTable.insertOrUpdate(userId,newAmount);

//        return new UserPoint(userId, amount, System.currentTimeMillis());
    }

    public UserPoint usePoint(long userId, long amount) {

        if (amount < 1000) {
            throw new  IllegalArgumentException("사용금액은 1000원 이상이여야 합니다");
        }

        // 현재 포인트 조회
        UserPoint currentUserPoint = userPointTable.selectById(userId);

        if (currentUserPoint.point() < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }

        // 포인트 차감
        long newPoint = currentUserPoint.point() - amount;

        // 포인트 저장
        UserPoint updatedUserPoint = userPointTable.insertOrUpdate(userId, newPoint);
        pointHistoryTable.insert(userId, amount, TransactionType.USE,updatedUserPoint.updateMillis());

        return updatedUserPoint;
    }

    public List<PointHistory> getUserPointHistory(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}