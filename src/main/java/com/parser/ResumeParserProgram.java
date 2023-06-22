package com.parser;

import java.awt.Rectangle;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parser.enums.RegEx;
import com.parser.service.extractor.educationsExtractor.EducationsExtractor;
import com.parser.service.extractor.experiencesExtractor.ExperiencesExtractor;
import com.parser.utils.ExtractUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResumeParserProgram {
	
	@Autowired
	private ExperiencesExtractor experienceExtractor;
	
	@Autowired
	private EducationsExtractor educationsExtractor;

	/**
	 * 
	 * @param cvText: String
	 * @return JSONObject: different parts of CV
	 */
	@SuppressWarnings("unchecked")
	public JSONObject loadData(String cvText) {

		JSONObject parsedJSON = new JSONObject();
		JSONObject profileJSON = new JSONObject();
		ResumeParserProgram.log.info("Started extractiong informations...");
		String experience = ExtractUtils.extractSection(cvText, RegEx.EXPERIENCE.name());
		profileJSON.put("name", ExtractUtils.extractInfo(cvText, ExtractUtils.namePattern));
		profileJSON.put("education", educationsExtractor.extractEducationsByDate(ExtractUtils.extractSection(cvText, RegEx.EDUCATION.name())));
		profileJSON.put("languages", ExtractUtils.extractSection(cvText, RegEx.LANGUAGES.name()));
		profileJSON.put("experiences", experienceExtractor.extractExperiences("Project"+experience.trim()));
		
		 
		if (!profileJSON.isEmpty()) {
			parsedJSON.put("basics", profileJSON);
		}
		return parsedJSON;
	}

	public String removeFooterIfExists(PDDocument document) throws IOException {
		ResumeParserProgram.log.info("checking if exists a footer ...");
		PDFTextStripper stripper = new PDFTextStripper();
		String pdfContent = stripper.getText(document);
		stripper.setStartPage(1); // Set the starting page
		stripper.setEndPage(1); // Set the ending page (same as starting page for a single page)
		// Set whether to suppress the extraction of footnotes
		stripper.setSuppressDuplicateOverlappingText(true);

		PDPage firstPage = document.getPage(0);
		// Define the coordinates of the footer area
		Rectangle footerArea = new Rectangle(0, (int) firstPage.getMediaBox().getHeight() - 100, (int) 600, 100); // Modify
																													// these

		// Create an instance of the PDFTextStripperByArea class
		PDFTextStripperByArea textStripper = new PDFTextStripperByArea();

		// Set the rectangle area for footer extraction
		textStripper.addRegion("footer", footerArea);
		String currentPageContent = stripper.getText(document);
		textStripper.extractRegions(document.getPage(0));
		textStripper.getTextForRegion("footer").strip();

		String footer = textStripper.getTextForRegion("footer").strip();
		if (footer!=null && !footer.isBlank()) {
			boolean hasFooter = currentPageContent.contains(footer);
			int i = 1;
			while (i < document.getNumberOfPages() && hasFooter) {
				i += 1;
				stripper.setStartPage(i);// Set the starting page
				stripper.setEndPage(i);// Set the ending page (same as starting page for a single page)
				currentPageContent = stripper.getText(document);

				// Check if the page text matches the footer pattern
				hasFooter = currentPageContent.contains(footer);

			}

			if (hasFooter) {
				ResumeParserProgram.log.info("remove footer: " + footer);
				String newText = pdfContent.replace(footer, " ");

				return newText;
			}
		}
		
		return pdfContent;
	}

}
