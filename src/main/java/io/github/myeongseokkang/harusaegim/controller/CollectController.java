package io.github.myeongseokkang.harusaegim.controller;

import io.github.myeongseokkang.harusaegim.dto.NotificationUploadRequest;
import io.github.myeongseokkang.harusaegim.dto.PhotoUploadRequest;
import io.github.myeongseokkang.harusaegim.service.CollectorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collect")
public class CollectController {
    private final CollectorService collectorService;
    public CollectController(CollectorService collectorService) { this.collectorService = collectorService; }

    @PostMapping("/notification")
    public ResponseEntity<Void> notification(@RequestAttribute("userId") Long userId, @Valid @RequestBody NotificationUploadRequest req) {
        collectorService.saveNotification(userId, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/photo")
    public ResponseEntity<Void> photo(@RequestAttribute("userId") Long userId, @Valid @RequestBody PhotoUploadRequest req) {
        collectorService.savePhoto(userId, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notifications/batch")
    public ResponseEntity<Void> notificationsBatch(@RequestAttribute("userId") Long userId, @Valid @RequestBody List<NotificationUploadRequest> list) {
        collectorService.saveNotifications(userId, list);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/photos/batch")
    public ResponseEntity<Void> photosBatch(@RequestAttribute("userId") Long userId, @Valid @RequestBody List<PhotoUploadRequest> list) {
        collectorService.savePhotos(userId, list);
        return ResponseEntity.ok().build();
    }
}
