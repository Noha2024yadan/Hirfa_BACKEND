package com.HIRFA.HIRFA.Statistiques;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/statistiques")
public class StatistiquesController {

    private final StatistiquesService statsService;

    public StatistiquesController(StatistiquesService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Long>> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
