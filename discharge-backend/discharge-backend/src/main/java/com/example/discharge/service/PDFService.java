package com.example.discharge.service;

import com.example.discharge.model.PatientRecord;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import org.springframework.stereotype.Service;

@Service
public class PDFService {

    public String generatePDF(PatientRecord record) {
        try {
            String filePath = "discharge_" + record.getPatientName() + ".pdf";

            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // ✅ Load multilingual font
            PdfFont font = loadFontByLanguage(record.getLanguage());
            document.setFont(font);

            // HEADER
            document.add(new Paragraph("MEDICAL DISCHARGE SUMMARY")
                    .setBold()
                    .setFontSize(18));

            document.add(new LineSeparator(new SolidLine()));

            // PATIENT INFO
            document.add(new Paragraph("\nPatient Name: " + record.getPatientName())
                    .setBold());

            // SUMMARY
            document.add(new Paragraph("\nClinical Details & Course of Treatment:")
                    .setBold()
                    .setUnderline());

            document.add(new Paragraph(record.getGeneratedSummary())
                    .setMultipliedLeading(1.5f));

            // MEDICINES
            if (record.getMedicines() != null && !record.getMedicines().isEmpty()) {
                document.add(new Paragraph("\nPrescribed Medications:")
                        .setBold()
                        .setUnderline());

                document.add(new Paragraph(record.getMedicines()));
            }

            // FOOTER
            document.add(new Paragraph("\n\nDoctor Signature: __________________"));
            document.add(new Paragraph("Hospital Seal: __________________"));

            document.close();

            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            return "PDF Generation Error: " + e.getMessage();
        }
    }

    // ✅ Dynamic font loader
    private PdfFont loadFontByLanguage(String language) throws Exception {

        String fontPath = "/fonts/NotoSans-Regular.ttf";

        if (language == null) language = "english";

        switch (language.toLowerCase()) {
            case "hindi":
            case "marathi":
                fontPath = "/fonts/NotoSansDevanagari-Regular.ttf";
                break;
            case "telugu":
                fontPath = "/fonts/NotoSansTelugu-Regular.ttf";
                break;
            case "tamil":
                fontPath = "/fonts/NotoSansTamil-Regular.ttf";
                break;
            case "kannada":
                fontPath = "/fonts/NotoSansKannada-Regular.ttf";
                break;
            case "malayalam":
                fontPath = "/fonts/NotoSansMalayalam-Regular.ttf";
                break;
            case "bengali":
                fontPath = "/fonts/NotoSansBengali-Regular.ttf";
                break;
            case "gujarati":
                fontPath = "/fonts/NotoSansGujarati-Regular.ttf";
                break;
            case "punjabi":
                fontPath = "/fonts/NotoSansGurmukhi-Regular.ttf";
                break;
            case "urdu":
                fontPath = "/fonts/NotoNaskhArabic-Regular.ttf";
                break;
        }

        return PdfFontFactory.createFont(
                getClass().getResource(fontPath).toExternalForm(),
                PdfEncodings.IDENTITY_H,
                PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
        );
    }
}