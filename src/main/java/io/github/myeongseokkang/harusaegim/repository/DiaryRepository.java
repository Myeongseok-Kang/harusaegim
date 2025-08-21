package io.github.myeongseokkang.harusaegim.repository;

import io.github.myeongseokkang.harusaegim.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByUserIdAndDate(Long userId, LocalDate date);
    List<Diary> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate start, LocalDate end); // 리포트 만들 때 사용
    List<Diary> findByUserIdOrderByDateDesc(Long userId); // 최근 일기 목록 가져옴
}