package com.fitness.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WhatsAppService {

    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);

    @Value("${whatsapp.api.url:}")
    private String apiUrl;

    @Value("${whatsapp.api.token:}")
    private String apiToken;

    @Value("${whatsapp.from.number:+5511950544316}")
    private String fromNumber;

    public void sendMessage(String to, String message) {
        logger.info("Enviando mensagem WhatsApp para {}: {}", to, message);
        
        // Se não houver configuração de API, apenas loga (modo teste/mock)
        if (apiUrl == null || apiUrl.isEmpty() || apiToken == null || apiToken.isEmpty()) {
            logger.warn("Configurações de WhatsApp não encontradas. Mensagem não enviada via API.");
            return;
        }

        try {
            // Exemplo genérico de chamada para Meta Cloud API (pode precisar de ajuste no JSON conforme o provider)
            String json = String.format("{\"messaging_product\": \"whatsapp\", \"to\": \"%s\", \"type\": \"text\", \"text\": {\"body\": \"%s\"}}", 
                to.replaceAll("[^0-9]", ""), message.replace("\n", "\\n"));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Authorization", "Bearer " + apiToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                logger.info("WhatsApp enviado com sucesso!");
            } else {
                logger.error("Erro ao enviar WhatsApp: {} - {}", response.statusCode(), response.body());
                throw new RuntimeException("Erro ao enviar WhatsApp: " + response.body());
            }
        } catch (Exception e) {
            logger.error("Falha na integração com WhatsApp", e);
            throw new RuntimeException(e);
        }
    }
}
