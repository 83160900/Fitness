package com.fitness.service;

import com.fitness.domain.model.Exercise;
import com.fitness.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExerciseSyncService {

    private static final Logger log = LoggerFactory.getLogger(ExerciseSyncService.class);

    private final ExternalExerciseClient client;
    private final ExerciseRepository repository;

    public ExerciseSyncService(ExternalExerciseClient client, ExerciseRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    // Sincroniza a cada 6 horas por padrÃ£o (pode ser ajustado via env: exercises.sync.fixedDelay)
    @Scheduled(fixedDelayString = "${exercises.sync.fixedDelay:21600000}")
    public void scheduledSync() {
        try {
            seedCommonBatches();
        } catch (Exception e) {
            log.warn("[SYNC] Falha na sincronizacao de exercicios: {}", e.getMessage());
        }
    }

    @Async
    public void seedCommonBatches() {
        // Conjuntos comuns (pouco volume por rodada para nÃ£o estourar limite)
        List<String> muscles = List.of("chest", "back", "legs", "shoulders", "biceps", "triceps", "core");
        int pageSize = 25;
        for (String m : muscles) {
            syncByMuscle(m, pageSize, 1);
        }
    }

    public void syncByMuscle(String muscle, int limit, int page) {
        try {
            log.info("[SYNC] Iniciando busca para musculo='{}' (pagina {})", muscle, page);
            List<Map<String, Object>> result = client.search(null, muscle, limit, page);
            if (result == null || result.isEmpty()) {
                log.info("[SYNC] Nenhum resultado retornado para musculo='{}'", muscle);
                return;
            }
            int count = 0;
            for (Map<String, Object> item : result) {
                try {
                    upsertFromExternal(item);
                    count++;
                } catch (Exception e) {
                    log.error("[SYNC] Erro ao processar item: {}", e.getMessage());
                }
            }
            log.info("[SYNC] Sucesso: musculo='{}' sincronizados {} de {} registros", muscle, count, result.size());
        } catch (Exception e) {
            log.error("[SYNC] ERRO FATAL ao sincronizar musculo '{}': {}", muscle, e.getMessage(), e);
        }
    }

    public Exercise upsertFromExternal(Map<String, Object> m) {
        try {
            String externalId = getString(m, "id", getString(m, "_id", null));
            if (externalId == null) {
                log.warn("[SYNC] Item ignorado: externalId nulo. Mapa: {}", m);
                return null;
            }
            String name = getString(m, "name", getString(m, "exercise_name", "unknown"));
            
            String muscle = joinCandidates(m, List.of("primaryMuscles", "target", "bodyPart", "muscle"));
            String equipment = joinCandidates(m, List.of("equipment", "equipmentRequired"));
            
            String imageUrl = firstNonBlank(
                    getString(m, "image", null),
                    getString(m, "imageUrl", null),
                    getString(m, "gifUrl", null)
            );
            String videoUrl = firstNonBlank(
                    getString(m, "videoUrl", null),
                    getString(m, "videoURL", null),
                    getString(m, "video", null)
            );

            Exercise entity = repository.findByExternalId(externalId).orElse(new Exercise());
            entity.setExternalId(externalId);
            entity.setName(name);
            entity.setPrimaryMuscles(muscle);
            entity.setEquipment(equipment);
            entity.setImageUrl(imageUrl);
            entity.setVideoUrl(videoUrl);
            entity.setLastSyncedAt(java.time.LocalDateTime.now());
            
            Exercise saved = repository.save(entity);
            log.debug("[SYNC] Exercício salvo/atualizado: {} (ID: {})", name, externalId);
            return saved;
        } catch (Exception e) {
            log.error("[SYNC] Erro no upsert: {}", e.getMessage());
            throw e;
        }
    }

    private static String getString(Map<String, Object> m, String key, String def) {
        Object v = m.get(key);
        return v == null ? def : String.valueOf(v);
    }

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    private static String joinCandidates(Map<String, Object> m, List<String> keys) {
        for (String k : keys) {
            Object v = m.get(k);
            if (v == null) continue;
            if (v instanceof List<?> list) {
                return String.join(", ", list.stream().map(String::valueOf).toList());
            }
            return String.valueOf(v);
        }
        return null;
    }
}
