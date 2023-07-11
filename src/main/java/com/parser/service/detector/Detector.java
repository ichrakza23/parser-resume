package com.parser.service.detector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

public interface Detector {
	
	public String[] detectSentences(String text) throws FileNotFoundException, IOException;
	public default  Language detectLanguage(String text) throws FileNotFoundException, IOException {
		String folder = System.getProperty("user.dir");
        try (InputStream modelInputStream = new FileInputStream(folder+"/src/main/resources/sentence-detector/langdetect.bin")) {
            LanguageDetectorModel model =  new LanguageDetectorModel(modelInputStream);
            LanguageDetectorME detector = new LanguageDetectorME(model);
            return detector.predictLanguage(text);
        }
	};

}
