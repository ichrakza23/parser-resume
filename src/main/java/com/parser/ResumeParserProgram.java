package com.parser;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
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

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
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

	
	/**
	 * 
	 * @param cvText: String
	 * @return JSONObject: different parts of CV
	 */
	@SuppressWarnings("unchecked")
	public JSONObject loadData(String cvText) {
		
		JSONObject parsedJSON = new JSONObject();
		JSONObject profileJSON = new JSONObject();
		ResumeParserProgram.log.info("Started parsing...");
		String experience = ExtractUtils.extractSection(cvText,RegEx.EXPERIENCE.name());
		profileJSON.put("name", ExtractUtils.extractInfo(cvText, ExtractUtils.namePattern));
		profileJSON.put("phone", ExtractUtils.extractInfo(cvText, ExtractUtils.phonePattern));
		profileJSON.put("email", ExtractUtils.extractInfo(cvText, ExtractUtils.emailPattern));
		profileJSON.put("address", ExtractUtils.extractInfo(cvText,ExtractUtils.addressPattern));
		profileJSON.put("experience",experience );
		profileJSON.put("education", ExtractUtils.extractSection(cvText,RegEx.EDUCATION.name()));
		profileJSON.put("languages", ExtractUtils.extractSection(cvText,RegEx.LANGUAGES.name()));
		profileJSON.put("hobbies", ExtractUtils.extractSection(cvText,RegEx.INTERESTS.name()));
		profileJSON.put("personalProjects", ExtractUtils.extractSection(cvText,RegEx.PERSONALPROJECTS.name()));
		profileJSON.put("certifications", ExtractUtils.extractSection(cvText,RegEx.CERTIFICATIONS.name()));
		profileJSON.put("scores", ExtractUtils.extractSection(cvText,RegEx.SCORES.name()));
		profileJSON.put("experiences",ExtractUtils.setExperiences(experience));

		if (!profileJSON.isEmpty()) {
			parsedJSON.put("basics", profileJSON);
		}
		return parsedJSON;
	}
	
public String removeFooterIfExists(PDDocument document) throws IOException {
	PDFTextStripper stripper = new PDFTextStripper();
	String pdfContent = stripper.getText(document);
	stripper.setStartPage(1); // Set the starting page
	stripper.setEndPage(1); // Set the ending page (same as starting page for a single page)
	// Set whether to suppress the extraction of footnotes
	stripper.setSuppressDuplicateOverlappingText(true);
	
	PDPage p = document.getPage(0);
	// Define the coordinates of the footer area
    Rectangle footerArea = new Rectangle(0,  (int) p.getMediaBox().getHeight()-100, (int) 600, 100); // Modify these values based on your PDF layout
    
    // Create an instance of the PDFTextStripperByArea class
    PDFTextStripperByArea textStripper = new PDFTextStripperByArea();

    // Set the rectangle area for footer extraction
    textStripper.addRegion("footer", footerArea);
	String currentPageContent = stripper.getText(document);
	textStripper.extractRegions(document.getPage(0));
	textStripper.getTextForRegion("footer").strip();
   
	String footer = textStripper.getTextForRegion("footer").strip();
    boolean hasFooter = currentPageContent.contains(footer);
    int i = 1;
    while ( i<document.getNumberOfPages() && hasFooter ) {
    	i+=1;
    	stripper.setStartPage(i);// Set the starting page
    	stripper.setEndPage(i);// Set the ending page (same as starting page for a single page)
    	currentPageContent = stripper.getText(document);

        // Check if the page text matches the footer pattern
        hasFooter = currentPageContent.contains(footer);
    	
    }

    if(hasFooter) {
    	ResumeParserProgram.log.info("to modify: " + footer);
    	String newText=pdfContent.replace(footer, " ");
    	ResumeParserProgram.log.info("Original String: " + pdfContent);
    	ResumeParserProgram.log.info("Replaced String: " + newText);
    		
    	return newText;
    }
    return pdfContent;
	}

}
