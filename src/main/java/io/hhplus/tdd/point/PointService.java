package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    private static final int MAX_DAILY_USE_COUNT = 5;
    private static final long MAX_DAILY_CHARGE_AMOUNT = 300_000L;

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

        List<PointHistory> historyList = pointHistoryTable.selectAllByUserId(userId);

        long todayStart = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        long todayChargeSum = historyList.stream()
                .filter(history -> history.type() == TransactionType.CHARGE)
                .filter(history -> history.updateMillis() >= todayStart)
                .mapToLong(PointHistory::amount)
                .sum();

        // 이번 충전까지 포함한 총 금액
        if (todayChargeSum + amount > MAX_DAILY_CHARGE_AMOUNT) {
            throw new IllegalArgumentException("일일 충전 한도(300,000원)를 초과했습니다");
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

        // 1. 전체 히스토리 조회
        List<PointHistory> allHistory = pointHistoryTable.selectAllByUserId(userId);

        // 2. 오늘 00시 시작 시점
        long todayStart = LocalDate.now().atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli();

        // 3. 필터링: todayStart <= updateMillis < 현재시간
        long todayUseCount = allHistory.stream()
                .filter(history -> history.type() == TransactionType.USE)      // USE만
                .filter(history -> history.updateMillis() >= todayStart)       // 당일만
                .count();

        // 4. 5회 이상이면 예외 발생
        if (todayUseCount >= MAX_DAILY_USE_COUNT) {
            throw new IllegalArgumentException("하루 포인트 사용 횟수(" + MAX_DAILY_USE_COUNT + "회)를 초과했습니다");
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