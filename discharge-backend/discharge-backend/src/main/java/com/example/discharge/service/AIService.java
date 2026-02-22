package com.example.discharge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class AIService {

    private final WebClient webClient;
    private final String googleApiKey;

    @Value("${groq.api.key}")
    private String groqApiKey;

    public AIService(
            WebClient.Builder webClientBuilder,
            @Value("${google.translate.api.key}") String googleApiKey) {
        this.webClient = webClientBuilder.build();
        this.googleApiKey = googleApiKey;
    }

    // ================= SUMMARY =================
    public String generateSummary(String rawText) {
        if (rawText == null || rawText.trim().isEmpty()) return "No content found.";

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content",
                "Extract ONLY clinical details from this medical OCR text. " +
                "Include Patient Info, Diagnosis, Medications (Dose/Frequency), Follow-up. " +
                "Ignore noise. Raw Text: " + rawText);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));

        return callGroqAPI(body);
    }

    // ================= GOOGLE TRANSLATE =================
    public String translateSummary(String text, String languageCode) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("q", text);
            body.put("target", languageCode); // directly pass ISO code
            body.put("format", "text");

            Map response = webClient.post()
                    .uri("https://translation.googleapis.com/language/translate/v2?key=" + googleApiKey)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null && response.containsKey("data")) {
                Map data = (Map) response.get("data");
                List translations = (List) data.get("translations");
                if (translations != null && !translations.isEmpty()) {
                    Map first = (Map) translations.get(0);
                    return first.get("translatedText").toString();
                }
            }
            return "Translation failed.";
        } catch (Exception e) {
            return "Translation error: " + e.getMessage();
        }
    }

    // ================= MEDICINE EXTRACTION =================
    public String extractMedicines(String rawText) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "List ONLY medications with dosage and frequency: " + rawText);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));

        return callGroqAPI(body);
    }

    // ================= REMINDER =================
    public String generateReminder(String summary) {
        summary = summary.toLowerCase();
        if (summary.contains("follow")) return "Reminder: Visit hospital for follow-up.";
        if (summary.contains("7 days")) return "Reminder: Visit hospital after 7 days.";
        return "No follow-up mentioned.";
    }

    // ================= CHAT =================
    public String chatWithGroq(String summary, String question) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Context: " + summary + "\nQuestion: " + question);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama-3.3-70b-versatile");
        body.put("messages", List.of(message));

        return callGroqAPI(body);
    }

    // ================= GROQ HELPER =================
    private String callGroqAPI(Map<String, Object> body) {
        Map response = webClient.post()
                .uri("https://api.groq.com/openai/v1/chat/completions")
                .header("Authorization", "Bearer " + groqApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response != null && response.containsKey("choices")) {
            List choices = (List) response.get("choices");
            Map first = (Map) choices.get(0);
            Map messageObj = (Map) first.get("message");
            return messageObj.get("content").toString();
        }
        return "AI failed.";
    }
}