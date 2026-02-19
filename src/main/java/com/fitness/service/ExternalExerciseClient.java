package com.fitness.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ExternalExerciseClient {

    private final RestTemplate restTemplate;

    @Value("${rapidapi.key:}")
    private String rapidApiKey;

    @Value("${rapidapi.host:edb-with-videos-and-images-by-ascendapi.p.rapidapi.com}")
    private String rapidApiHost;

    @Value("${rapidapi.baseUrl:https://edb-with-videos-and-images-by-ascendapi.p.rapidapi.com}")
    private String baseUrl;

    public ExternalExerciseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> search(String query, String muscle, Integer limit, Integer page) {
        if (rapidApiKey == null || rapidApiKey.isBlank()) {
            String envKey = System.getenv("RAPIDAPI_KEY");
            if (envKey != null && !envKey.isBlank()) rapidApiKey = envKey;
        }
        if (rapidApiKey == null || rapidApiKey.isBlank()) {
            throw new IllegalStateException("RapidAPI key não configurada. Defina 'rapidapi.key' ou a env 'RAPIDAPI_KEY'.");
        }

        StringBuilder url = new StringBuilder(baseUrl).append("/exercises");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (query != null && !query.isBlank()) params.add("name", query);
        if (muscle != null && !muscle.isBlank()) params.add("muscle", muscle);
        if (limit != null) params.add("limit", String.valueOf(limit));
        if (page != null) params.add("page", String.valueOf(page));

        if (!params.isEmpty()) {
            url.append("?");
            url.append(params.entrySet().stream()
                    .flatMap(e -> e.getValue().stream().map(v -> e.getKey() + "=" + v))
                    .reduce((a, b) -> a + "&" + b).orElse(""));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-RapidAPI-Key", rapidApiKey);
        headers.add("X-RapidAPI-Host", rapidApiHost);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, List.class);
        List body = response.getBody();
        if (body == null) return Collections.emptyList();
        return body;
    }
}
