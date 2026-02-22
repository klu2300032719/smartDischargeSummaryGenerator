package com.example.discharge.controller;

import com.example.discharge.service.DischargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") 
public class DischargeController {

    @Autowired
    private DischargeService dischargeService;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(
            @RequestParam String patientName,
            @RequestParam MultipartFile file,
            @RequestParam(defaultValue = "english") String language) {
        try {
            File temp = File.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(temp);
            String summary = dischargeService.processFile(patientName, temp, language);
            temp.delete();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        return ResponseEntity.ok(dischargeService.chat(question));
    }

    @GetMapping("/download/{patientName}")
    public ResponseEntity<Resource> download(@PathVariable String patientName) {
        try {
            Path path = Paths.get(System.getProperty("user.dir")).resolve("discharge_" + patientName + ".pdf");
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}