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
        
        System.out.println("[DEBUG_LOG] API: Tentando busca de exercicios...");
        if (rapidApiKey == null || rapidApiKey.isBlank()) {
            System.err.println("[DEBUG_LOG] API: ERRO - RAPIDAPI_KEY nao encontrada no sistema.");
            throw new IllegalStateException("RapidAPI key nÃ£o configurada. Defina 'rapidapi.key' ou a env 'RAPIDAPI_KEY'.");
        }

        String keyPrefix = rapidApiKey.length() > 4 ? rapidApiKey.substring(0, 4) : "***";
        System.out.println("[DEBUG_LOG] API: Usando chave iniciando em: " + keyPrefix + "****");

        // Fixed endpoint: testing /list or /exercises based on 404 logs
        // Many RapidAPIs for exercises use /exercises or /list. 
        // We'll try to build the URL more carefully.
        StringBuilder url = new StringBuilder(baseUrl);
        if (!baseUrl.endsWith("/")) url.append("/");
        
        // Based on the 404 error, /exercises was not found. 
        // For this AscendAPI exercise database, the base endpoints are usually:
        // /exercisedb/list (or similar). Let's use the provided baseUrl strictly.
        // We'll try to append "/" and if searching by muscle, use its specific endpoint pattern.
        
        if (muscle != null && !muscle.isBlank()) {
            url.append("exercises/muscle/").append(muscle);
        } else {
            url.append("exercises");
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (query != null && !query.isBlank()) params.add("name", query);
        // muscle already added to path
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
        System.out.println("[DEBUG_LOG] API: Chamando URL: " + url.toString());
        
        try {
            ResponseEntity<String> responseRaw = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, String.class);
            String responseBody = responseRaw.getBody();
            System.out.println("[DEBUG_LOG] API: Resposta bruta recebida (parcial): " + (responseBody != null && responseBody.length() > 200 ? responseBody.substring(0, 200) : responseBody));

            if (responseBody == null || responseBody.trim().isEmpty() || responseBody.trim().equals("[]")) {
                System.out.println("[DEBUG_LOG] API: Resposta vazia ou lista vazia [] recebida da RapidAPI.");
                return Collections.emptyList();
            }

            // Tentar converter de String para List<Map>
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            List<Map<String, Object>> body = mapper.readValue(responseBody, new com.fasterxml.jackson.core.type.TypeReference<List<Map<String, Object>>>() {});
            
            System.out.println("[DEBUG_LOG] API: Sucesso! Parse realizado em " + body.size() + " itens.");
            return body;
        } catch (Exception e) {
            System.err.println("[DEBUG_LOG] API: ERRO NA REQUISICAO - " + e.getClass().getSimpleName() + " : " + e.getMessage());
            if (e instanceof org.springframework.web.client.HttpStatusCodeException sce) {
                System.err.println("[DEBUG_LOG] API: Status HTTP: " + sce.getStatusCode() + " | Resposta: " + sce.getResponseBodyAsString());
            }
            throw new RuntimeException("Erro ao buscar exercicios na RapidAPI: " + e.getMessage(), e);
        }
    }
}
