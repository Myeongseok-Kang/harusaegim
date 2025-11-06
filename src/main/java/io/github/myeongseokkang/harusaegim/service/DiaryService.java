package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.entity.Diary;
import io.github.myeongseokkang.harusaegim.entity.NotificationEvent;
import io.github.myeongseokkang.harusaegim.entity.PhotoEvent;
import io.github.myeongseokkang.harusaegim.dto.DiaryUpdateRequest;
import io.github.myeongseokkang.harusaegim.repository.DiaryRepository;
import io.github.myeongseokkang.harusaegim.repository.NotificationEventRepository;
import io.github.myeongseokkang.harusaegim.repository.PhotoEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final NotificationEventRepository notificationRepo;
    private final PhotoEventRepository photoRepo;

    public DiaryService(DiaryRepository diaryRepository, NotificationEventRepository notificationRepo, PhotoEventRepository photoRepo) {
        this.diaryRepository = diaryRepository;
        this.notificationRepo = notificationRepo;
        this.photoRepo = photoRepo;
    }

    @Transactional
    public Diary generate(Long userId, LocalDate date) {
        Diary existing = diaryRepository.findByUserIdAndDate(userId, date).orElse(null);
        if (existing != null) return existing;
        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        List<NotificationEvent> notis = notificationRepo.findByUserIdAndPostedAtBetween(userId, start, end);
        List<PhotoEvent> photos = photoRepo.findByUserIdAndTakenAtBetween(userId, start, end);

        int score = scoreEmotion(notis, photos);
        String content = compose(date, notis, photos, score);

        Diary d = new Diary();
        d.setUserId(userId);
        d.setDate(date);
        d.setContent(content);
        d.setEmotionScore(score);
        return diaryRepository.save(d);
    }

    public List<Diary> list(Long userId) { return diaryRepository.findByUserIdOrderByDateDesc(userId); }

    public Diary get(Long userId, Long id) {
        Diary d = diaryRepository.findById(id).orElseThrow();
        if (!d.getUserId().equals(userId)) throw new IllegalStateException("forbidden");
        return d;
    }

    @Transactional
    public Diary update(Long userId, Long id, DiaryUpdateRequest req) {
        Diary d = get(userId, id);
        d.setContent(req.getContent());
        if (req.getEmotionScore() != null) d.setEmotionScore(req.getEmotionScore());
        return d;
    }

    @Transactional
    public void delete(Long userId, Long id) {
        Diary d = get(userId, id);
        diaryRepository.delete(d);
    }

    private int scoreEmotion(List<NotificationEvent> notis, List<PhotoEvent> photos) {
        String all = notis.stream().map(n -> (n.getTitle() == null ? "" : n.getTitle()) + " " + (n.getText() == null ? "" : n.getText())).collect(Collectors.joining(" ")).toLowerCase();
        String[] pos = {"행복", "좋음", "기쁨", "축하", "love", "happy", "win", "success", "좋아", "감사"};
        String[] neg = {"슬픔", "우울", "짜증", "화남", "실패", "error", "fail", "angry", "불편", "피곤"};
        int p = 0;
        for (String s : pos) if (all.contains(s)) p += 2;
        for (String s : neg) if (all.contains(s)) p -= 2;
        p += Math.min(photos.size(), 10);
        if (p > 20) p = 20;
        if (p < -20) p = -20;
        return p;
    }

    private String compose(LocalDate date, List<NotificationEvent> notis, List<PhotoEvent> photos, int score) {
        StringJoiner j = new StringJoiner("\n");
        j.add(date.toString() + "의 기록");
        List<NotificationEvent> top = notis.stream().sorted(Comparator.comparing(NotificationEvent::getPostedAt)).limit(5).toList();
        if (!top.isEmpty()) {
            j.add("오늘 받은 알림 " + notis.size() + "개 중 일부");
            for (NotificationEvent n : top) {
                String t = n.getTitle() == null ? "" : n.getTitle();
                String x = n.getText() == null ? "" : n.getText();
                String line = (t + " " + x).trim();
                if (line.length() > 80) line = line.substring(0, 80) + "...";
                j.add("- " + line);
            }
        }
        if (!photos.isEmpty()) {
            long withGps = photos.stream().filter(p -> p.getLatitude() != null && p.getLongitude() != null).count();
            j.add("사진 " + photos.size() + "장, 위치 포함 " + withGps + "장");
        }
        String mood = score >= 8 ? "매우 좋음" : score >= 3 ? "보통보다 좋음" : score >= -2 ? "보통" : score >= -7 ? "조금 다운" : "많이 다운";
        j.add("오늘의 기분 지수 " + score + " (" + mood + ")");
        j.add("내일은 가벼운 산책과 일찍 자기");
        return j.toString();
    }
}
