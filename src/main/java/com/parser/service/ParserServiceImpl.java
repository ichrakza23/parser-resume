package com.parser.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.parser.ResumeParserProgram;
import com.parser.utils.DocumentToHtmlConverter;
import com.parser.wrapper.ResponseWrapper;

import exception.BadFileFormatException;
import exception.FileHandlerException;

/**
 * 
 * @author izarati
 *
 */

@Service
public class ParserServiceImpl implements ParserService {

	private static final String userDir = "user.dir";
	private static final String resumesFolder = "/Resumes/";

	@Autowired
	private ResumeParserProgram resumeParserProgram;

	@Override
	public ResponseWrapper parseResume(MultipartFile file) throws NoSuchFileException,FileHandlerException, BadFileFormatException {

		String uploadedFolder = System.getProperty(userDir);
		if (uploadedFolder != null && !uploadedFolder.isEmpty()) {
			uploadedFolder += resumesFolder;
		} else
			throw new NoSuchFileException("User Directory not found");
		ResponseWrapper responseWrapper = null;
		byte[] bytes = null;
		try {
			bytes = file.getBytes();
		} catch (IOException exception) {
			throw new FileHandlerException("An error occurs when trying to read the file");
		}
		Path path = null;
		try {
			path = Paths.get(uploadedFolder + file.getOriginalFilename());
			if (!Files.exists(path.getParent()))
				Files.createDirectories(path.getParent());
			path = Files.write(path, bytes);
		} catch (IOException exception) {
			throw new FileHandlerException(exception.getMessage());

		}
		JSONObject parsedJSON = null;
		File filetoExtractData = new File(
				DocumentToHtmlConverter.parseToPDFUsingConvertApi(path.toAbsolutePath().toString()));
		File tikkaConvertedFile = null;
		try {
			tikkaConvertedFile = DocumentToHtmlConverter.parseToHTMLUsingApacheTikka(path.toAbsolutePath().toString());
		} catch (SAXException exception) {
			throw new FileHandlerException("An error occurs when trying to parse the file to html");

		}
		PDDocument document;
		if (tikkaConvertedFile != null) {
			try {
				document = PDDocument.load(filetoExtractData);
				String content = resumeParserProgram.removeFooterIfExists(document);

				parsedJSON = resumeParserProgram.loadData(content);
			} catch (IOException e) {
				throw new FileHandlerException("An error occurs when trying to extract data");
			}
			responseWrapper = new ResponseWrapper();
			responseWrapper.setStatus(200);
			responseWrapper.setData(parsedJSON);
			responseWrapper.setMessage("Successfully parsed Resume!");
		}

		return responseWrapper;
	}

}
