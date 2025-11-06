package io.github.myeongseokkang.harusaegim.controller;

import io.github.myeongseokkang.harusaegim.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    public ReportController(ReportService reportService) { this.reportService = reportService; }

    @GetMapping("/weekly")
    public ResponseEntity<List<Map<String, Object>>> weekly(@RequestAttribute("userId") Long userId,
                                                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate end = endDate == null ? LocalDate.now() : endDate;
        return ResponseEntity.ok(reportService.weekly(userId, end));
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<Map<String, Object>>> monthly(@RequestAttribute("userId") Long userId,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate end = endDate == null ? LocalDate.now() : endDate;
        return ResponseEntity.ok(reportService.monthly(userId, end));
    }
}
