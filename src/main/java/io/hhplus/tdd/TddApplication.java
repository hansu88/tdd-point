package io.hhplus.tdd;

import io.hhplus.tdd.database.UserPointTable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TddApplication {

    public static void main(String[] args) {
        SpringApplication.run(TddApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(UserPointTable userPointTable) {
        return args -> {
            // 테스트용 초기 데이터
            userPointTable.insertOrUpdate(1L, 1000L);
            userPointTable.insertOrUpdate(2L, 0L);
            userPointTable.insertOrUpdate(3L, 0L);
            System.out.println("✅ 테스트 데이터 초기화 완료!");
        };
    }
}
