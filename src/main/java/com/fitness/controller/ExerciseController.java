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

    @RequestMapping(value = "/sync", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> triggerSync() {
        try {
            System.out.println("[DEBUG_LOG] Sync: Recebido pedido de sincronizacao (Manual/GET/POST)...");
            syncService.seedCommonBatches();
            System.out.println("[DEBUG_LOG] Sync: Sincronizacao disparada com sucesso.");
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Sincronizacao de exercicios iniciada com sucesso em segundo plano!",
                "timestamp", java.time.Instant.now().toString()
            ));
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] Sync: ERRO AO DISPARAR - " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "status", "error",
                "message", "Falha ao disparar sincronizacao: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus() {
        long total = repository.count();
        java.time.Instant lastSync = repository.findLastSyncTimestamp().orElse(null);
        return ResponseEntity.ok(Map.of(
            "total_exercicios_baixados", total,
            "ultima_sincronizacao", lastSync != null ? lastSync.toString() : "Nenhuma",
            "mensagem", total > 0 ? "Sincronizacao em andamento ou concluida." : "Nenhum exercicio no banco ainda. Verifique se a RAPIDAPI_KEY esta correta.",
            "timestamp_atual", java.time.Instant.now().toString()
        ));
    }

    @PostMapping("/ensure")
    public ResponseEntity<?> ensureExercises(@RequestBody List<Map<String, String>> exercises) {
        for (Map<String, String> exData : exercises) {
            String name = exData.get("name");
            if (name == null || name.isEmpty()) continue;
            
            if (repository.findByNameIgnoreCase(name).isEmpty()) {
                Exercise ex = new Exercise();
                ex.setName(name);
                ex.setPrimaryMuscles(exData.getOrDefault("category", "Geral"));
                ex.setSource("manual_seed");
                repository.save(ex);
            }
        }
        return ResponseEntity.ok(Map.of("message", "Exercícios garantidos no banco"));
    }
}
