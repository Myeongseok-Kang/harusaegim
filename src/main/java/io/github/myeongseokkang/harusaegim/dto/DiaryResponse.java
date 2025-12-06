package io.github.myeongseokkang.harusaegim.dto;

import io.github.myeongseokkang.harusaegim.entity.Diary;

import java.time.Instant;
import java.time.LocalDate;

public record DiaryResponse(
        Long id,
        LocalDate date,
        String content,
        Integer emotionScore,
        Instant createdAt,
        Instant updatedAt
) {
    public static DiaryResponse from(Diary diary) {
        return new DiaryResponse(
                diary.getId(),
                diary.getDate(),
                diary.getContent(),
                diary.getEmotionScore(),
                diary.getCreatedAt(),
                diary.getUpdatedAt()
        );
    }
}
