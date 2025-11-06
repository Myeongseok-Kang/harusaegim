package io.github.myeongseokkang.harusaegim.controller;

import io.github.myeongseokkang.harusaegim.entity.Diary;
import io.github.myeongseokkang.harusaegim.dto.DiaryUpdateRequest;
import io.github.myeongseokkang.harusaegim.service.DiaryService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {
    private final DiaryService diaryService;
    public DiaryController(DiaryService diaryService) { this.diaryService = diaryService; }

    @PostMapping("/generate")
    public ResponseEntity<Diary> generate(@RequestAttribute("userId") Long userId,
                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate d = date == null ? LocalDate.now() : date;
        return ResponseEntity.ok(diaryService.generate(userId, d));
    }

    @GetMapping
    public ResponseEntity<List<Diary>> list(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(diaryService.list(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diary> get(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(diaryService.get(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Diary> update(@RequestAttribute("userId") Long userId, @PathVariable Long id, @Valid @RequestBody DiaryUpdateRequest req) {
        return ResponseEntity.ok(diaryService.update(userId, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestAttribute("userId") Long userId, @PathVariable Long id) {
        diaryService.delete(userId, id);
        return ResponseEntity.ok().build();
    }
}
