package io.github.myeongseokkang.harusaegim.controller;

import io.github.myeongseokkang.harusaegim.dto.DiaryCreateRequest;
import io.github.myeongseokkang.harusaegim.dto.DiaryResponse;
import io.github.myeongseokkang.harusaegim.dto.DiaryUpdateRequest;
import io.github.myeongseokkang.harusaegim.service.DiaryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public ResponseEntity<DiaryResponse> create(@RequestAttribute("userId") Long userId,
                                                @Valid @RequestBody DiaryCreateRequest req) {
        return ResponseEntity.ok(diaryService.create(userId, req));
    }

    @GetMapping
    public ResponseEntity<List<DiaryResponse>> list(@RequestAttribute("userId") Long userId) {
        return ResponseEntity.ok(diaryService.list(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryResponse> get(@RequestAttribute("userId") Long userId,
                                             @PathVariable Long id) {
        return ResponseEntity.ok(diaryService.get(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiaryResponse> update(@RequestAttribute("userId") Long userId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody DiaryUpdateRequest req) {
        return ResponseEntity.ok(diaryService.update(userId, id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestAttribute("userId") Long userId,
                                       @PathVariable Long id) {
        diaryService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}
