package com.fitness.controller;

import com.fitness.domain.model.Exercise;
import com.fitness.repository.ExerciseRepository;
import com.fitness.service.ExerciseSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    private final ExerciseRepository repository;
    private final ExerciseSyncService syncService;

    public ExerciseController(ExerciseRepository repository, ExerciseSyncService syncService) {
        this.repository = repository;
        this.syncService = syncService;
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> search(@RequestParam(required = false) String q,
                                                 @RequestParam(required = false) String muscle,
                                                 @RequestParam(required = false) String equipment) {
        List<Exercise> list = repository.search(q, muscle, equipment);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API de Exercicios operacional!");
    }

    @PostMapping("/sync")
    public ResponseEntity<?> triggerSync() {
        try {
            System.out.println("[DEBUG_LOG] Sync: Recebido pedido de sincronizacao manual...");
            syncService.seedCommonBatches();
            System.out.println("[DEBUG_LOG] Sync: Sincronizacao disparada com sucesso.");
            return ResponseEntity.accepted().body(Map.of("message", "Sincronizacao iniciada em segundo plano!"));
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Sync: ERRO AO DISPARAR - " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
