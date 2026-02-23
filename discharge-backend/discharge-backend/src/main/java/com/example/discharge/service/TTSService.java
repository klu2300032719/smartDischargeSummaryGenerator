package com.example.discharge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class TTSService {

    @Value("${google.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create();

    public byte[] generateSpeech(String text, String lang) {

        Map<String, Object> body = new HashMap<>();

        body.put("input", Map.of("text", text));

        body.put("voice", Map.of(
                "languageCode", lang,
                "ssmlGender", "FEMALE"
        ));

        body.put("audioConfig", Map.of(
                "audioEncoding", "MP3",
                "speakingRate", 0.9
        ));

        Map response = webClient.post()
                .uri("https://texttospeech.googleapis.com/v1/text:synthesize?key=" + apiKey)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        String audioContent = ((Map) response).get("audioContent").toString();

        return Base64.getDecoder().decode(audioContent);
    }
}