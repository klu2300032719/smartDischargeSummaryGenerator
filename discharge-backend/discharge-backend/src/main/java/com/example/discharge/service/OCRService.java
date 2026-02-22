package com.example.discharge.service;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

@Service
public class OCRService {

    public String extractText(File file) {
        try {

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

            BufferedImage image = ImageIO.read(file);

            if (image == null) {
                return "Invalid image file";
            }

            return tesseract.doOCR(image);

        } catch (Exception e) {
            e.printStackTrace();
            return "OCR Failed: " + e.getMessage();
        }
    }
}
