package com.fitness.service;

import com.fitness.domain.model.Exercise;
import com.fitness.repository.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    // Sincroniza a cada 6 horas por padrão (pode ser ajustado via env: exercises.sync.fixedDelay)
    @Scheduled(fixedDelayString = "${exercises.sync.fixedDelay:21600000}")
    public void scheduledSync() {
        try {
            seedCommonBatches();
        } catch (Exception e) {
            log.warn("[SYNC] Falha na sincronizacao de exercicios: {}", e.getMessage());
        }
    }

    public void seedCommonBatches() {
        // Conjuntos comuns (pouco volume por rodada para não estourar limite)
        List<String> muscles = List.of("chest", "back", "legs", "shoulders", "biceps", "triceps", "core");
        int pageSize = 25;
        for (String m : muscles) {
            syncByMuscle(m, pageSize, 1);
        }
    }

    public void syncByMuscle(String muscle, int limit, int page) {
        List<Map<String, Object>> result = client.search(null, muscle, limit, page);
        for (Map<String, Object> item : result) {
            upsertFromExternal(item);
        }
        log.info("[SYNC] Musculo='{}' sincronizados {} registros (pagina {})", muscle, result.size(), page);
    }

    public Exercise upsertFromExternal(Map<String, Object> m) {
        String externalId = getString(m, "id", getString(m, "_id", null));
        String name = getString(m, "name", getString(m, "exercise_name", "unknown"));
        // Tentativas de mapear campos comuns
        String muscle = joinCandidates(m,
                List.of("primaryMuscles", "target", "bodyPart", "muscle"));
        String equipment = joinCandidates(m,
                List.of("equipment", "equipmentRequired"));
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

        Exercise entity = repository.findByExternalId(externalId).orElse(
                new Exercise(externalId, name, muscle, equipment, imageUrl, videoUrl)
        );
        entity.setName(name);
        entity.setPrimaryMuscles(muscle);
        entity.setEquipment(equipment);
        entity.setImageUrl(imageUrl);
        entity.setVideoUrl(videoUrl);
        return repository.save(entity);
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
