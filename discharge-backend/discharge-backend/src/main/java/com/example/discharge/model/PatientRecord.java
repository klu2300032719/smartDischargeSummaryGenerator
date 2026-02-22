package com.example.discharge.model;

import jakarta.persistence.*;

@Entity
public class PatientRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String patientName;

    @Column(length = 100000)
    private String rawText;

    @Column(length = 100000)
    private String generatedSummary;

    @Column(columnDefinition = "TEXT")
    private String medicines;

    @Column(length = 50)
    private String language;   // ✅ added

    // ======================
    // GETTERS & SETTERS
    // ======================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getGeneratedSummary() {
        return generatedSummary;
    }

    public void setGeneratedSummary(String generatedSummary) {
        this.generatedSummary = generatedSummary;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}