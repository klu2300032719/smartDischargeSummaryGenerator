package com.example.discharge.repository;

import com.example.discharge.model.PatientRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientRecord, Long> {

    // ⭐ Needed for chatbot + reminder
    PatientRecord findTopByOrderByIdDesc();
}