package com.parser.service.imageExtractor;

import java.io.File;

import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

@Service
public class ImageOCR {

	public String extractTextFromImages(String imagePath) {
		ITesseract tesseract = new Tesseract();
		tesseract.setDatapath("src/main/resources/tessdata");
		tesseract.setLanguage("fra");
		String result = null;
		try {
			File imageFile = new File(imagePath);

			result = tesseract.doOCR(imageFile);
			System.out.println("OCR Result:");
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
