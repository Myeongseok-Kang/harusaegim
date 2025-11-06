package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.entity.Diary;
import io.github.myeongseokkang.harusaegim.repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {
    private final DiaryRepository diaryRepository;
    public ReportService(DiaryRepository diaryRepository) { this.diaryRepository = diaryRepository; }

    public List<Map<String, Object>> weekly(Long userId, LocalDate endDate) {
        LocalDate start = endDate.minusDays(6);
        return buildSeries(userId, start, endDate);
    }

    public List<Map<String, Object>> monthly(Long userId, LocalDate endDate) {
        LocalDate start = endDate.minusDays(29);
        return buildSeries(userId, start, endDate);
    }

    private List<Map<String, Object>> buildSeries(Long userId, LocalDate start, LocalDate end) {
        List<Diary> diaries = diaryRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId, start, end);
        Map<LocalDate, Integer> map = new HashMap<>();
        for (Diary d : diaries) map.put(d.getDate(), d.getEmotionScore() == null ? 0 : d.getEmotionScore());
        List<Map<String, Object>> out = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            Map<String, Object> row = new HashMap<>();
            row.put("date", d.toString());
            row.put("score", map.getOrDefault(d, null));
            out.add(row);
        }
        return out;
    }
}
