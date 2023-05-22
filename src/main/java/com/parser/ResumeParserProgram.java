package com.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import com.parser.enums.RegEx;
import com.parser.utils.ExtractUtils;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;
import gate.util.Out;

@Component
public class ResumeParserProgram {
	public File parseToHTMLUsingApacheTikka(String file) throws IOException, SAXException, TikaException {
		String ext = FilenameUtils.getExtension(file);
		String outputFileFormat = "";
		if (ext.equalsIgnoreCase("html") | ext.equalsIgnoreCase("pdf") | ext.equalsIgnoreCase("doc")
				| ext.equalsIgnoreCase("docx")) {
			outputFileFormat = ".html";
		} else if (ext.equalsIgnoreCase("txt") | ext.equalsIgnoreCase("rtf")) {
			outputFileFormat = ".txt";
		} else {
			System.out.println("Input format of the file " + file + " is not supported.");
			return null;
		}
		String OUTPUT_FILE_NAME = FilenameUtils.removeExtension(file) + outputFileFormat;
		ContentHandler handler = new ToXMLContentHandler();
		InputStream stream = new FileInputStream(file);
		AutoDetectParser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		try {
			parser.parse(stream, handler, metadata);
			FileWriter htmlFileWriter = new FileWriter(OUTPUT_FILE_NAME);
			htmlFileWriter.write(handler.toString());
			htmlFileWriter.flush();
			htmlFileWriter.close();
			return new File(OUTPUT_FILE_NAME);
		} finally {
			stream.close();
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject loadGateAndAnnie(File file) throws GateException, IOException {
		System.setProperty("gate.site.config", System.getProperty("user.dir")+"/GATEFiles/gate.xml");
		if (Gate.getGateHome() == null)
			Gate.setGateHome(new File(System.getProperty("user.dir")+"/GATEFiles"));
		if (Gate.getPluginsHome() == null)
			Gate.setPluginsHome(new File(System.getProperty("user.dir")+"/GATEFiles/plugins"));
		Gate.init();

		Annie annie = new Annie();
		annie.initAnnie();

		Corpus corpus = Factory.newCorpus("Annie corpus");
		URL u = file.toURI().toURL();
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl", u);
		params.put("preserveOriginalContent", new Boolean(true));
		params.put("collectRepositioningInfo", new Boolean(true));
		Out.prln("Creating doc for " + u);
		Document resume = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
		corpus.add(resume);

		annie.setCorpus(corpus);
		annie.execute();
		JSONObject parsedJSON = new JSONObject();
		JSONObject profileJSON = new JSONObject();
		Out.prln("Started parsing...");
		String cvText = resume.getContent().toString();// load the CV text here

		profileJSON.put("name", ExtractUtils.extractInfo(cvText, ExtractUtils.namePattern));
		profileJSON.put("phone", ExtractUtils.extractInfo(cvText, ExtractUtils.phonePattern));
		profileJSON.put("email", ExtractUtils.extractInfo(cvText, ExtractUtils.emailPattern));
		profileJSON.put("address", ExtractUtils.extractInfo(cvText,ExtractUtils.addressPattern));
		profileJSON.put("experience", ExtractUtils.extractSection(cvText,RegEx.EXPERIENCE.name()));
		profileJSON.put("education", ExtractUtils.extractSection(cvText,RegEx.EDUCATION.name()));
		profileJSON.put("languages", ExtractUtils.extractSection(cvText,RegEx.LANGUAGES.name()));
		profileJSON.put("hobbies", ExtractUtils.extractSection(cvText,RegEx.INTERESTS.name()));
		profileJSON.put("personalProjects", ExtractUtils.extractSection(cvText,RegEx.PERSONALPROJECTS.name()));
		profileJSON.put("certifications", ExtractUtils.extractSection(cvText,RegEx.CERTIFICATIONS.name()));
		profileJSON.put("scores", ExtractUtils.extractSection(cvText,RegEx.SCORES.name()));
		

		if (!profileJSON.isEmpty()) {
			parsedJSON.put("basics", profileJSON);
		}
		return parsedJSON;
	}

}
