package com.example.discharge.service;

import com.example.discharge.model.PatientRecord;
import com.example.discharge.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class DischargeService {

    @Autowired
    private OCRService ocrService;

    @Autowired
    private AIService aiService;

    @Autowired
    private PatientRepository repository;

    @Autowired
    private PDFService pdfService;

    public String processFile(String patientName, File file, String language) {
        String rawText = ocrService.extractText(file);

        // 1. Generate the initial summary
        String summary = aiService.generateSummary(rawText);
        
        // 2. Translate BOTH the summary and the medicine list if needed
        String translatedSummary = aiService.translateSummary(summary, language);
        String medicines = aiService.extractMedicines(rawText);
        String translatedMedicines = aiService.translateSummary(medicines, language);

        PatientRecord record = new PatientRecord();
        record.setPatientName(patientName);
        record.setRawText(rawText);
        
        // IMPORTANT: Set the translated versions so PDFService picks them up
        record.setGeneratedSummary(translatedSummary);
        record.setMedicines(translatedMedicines);

        repository.save(record);
        
        // 3. Generate PDF (now containing translated text)
        pdfService.generatePDF(record);

        return translatedSummary;
    }

    public String chat(String question) {

        PatientRecord record = repository.findTopByOrderByIdDesc();

        if (record == null)
            return "Please upload discharge report first.";

        return aiService.chatWithGroq(record.getGeneratedSummary(), question);
    }

    public String reminder() {
        PatientRecord record = repository.findTopByOrderByIdDesc();
        return aiService.generateReminder(record.getGeneratedSummary());
    }
}