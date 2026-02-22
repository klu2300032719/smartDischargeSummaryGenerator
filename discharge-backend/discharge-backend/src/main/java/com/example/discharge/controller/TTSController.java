package com.example.discharge.controller;

import com.example.discharge.service.TTSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow React & future mobile apps
public class TTSController {

    @Autowired
    private TTSService ttsService;

    /**
     * Converts text → speech using Google TTS
     * Returns MP3 audio stream to frontend
     */
    @GetMapping("/speak")
    public ResponseEntity<byte[]> speak(
            @RequestParam String text,
            @RequestParam(defaultValue = "en-IN") String lang) {

        try {
            // ⭐ Safety: avoid huge payloads causing buffer crash
            if (text.length() > 500) {
                text = text.substring(0, 500);
            }

            byte[] audio = ttsService.generateSpeech(text, lang);

            if (audio == null || audio.length == 0) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=speech.mp3")
                    .contentType(MediaType.parseMediaType("audio/mpeg"))
                    .contentLength(audio.length)
                    .body(audio);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}