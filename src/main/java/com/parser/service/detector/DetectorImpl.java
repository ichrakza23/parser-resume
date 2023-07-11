package com.parser.service.detector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

@Service
public class DetectorImpl implements Detector {

	@Override
	public String[] detectSentences(String text) throws FileNotFoundException, IOException {
		String folder = System.getProperty("user.dir");
		try (InputStream modelInputStream = new FileInputStream(
				folder + "/src/main/resources/sentence-detector/sentence_frensh_detector.bin")) {
			SentenceModel model = new SentenceModel(modelInputStream);
			SentenceDetectorME detector = new SentenceDetectorME(model);
			return detector.sentDetect(text);
		}
	}
}
