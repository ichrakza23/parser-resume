package com.parser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.SAXException;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;

import exception.BadFileFormatException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocumentToHtmlConverter {

	/**
	 * convert words doc to pdf
	 * 
	 * @param inputFile
	 * @return
	 * @throws Exception 
	 */
	public static String parseToPDFUsingConvertApi(String inputFile) throws BadFileFormatException {
		String ext = FilenameUtils.getExtension(inputFile);
		String outputFile = FilenameUtils.removeExtension(inputFile) + ".pdf";
		Param inputFileParam = null;
		if (Arrays.asList("docx", "doc", "pdf", "html").contains(ext.toLowerCase())) {
			try {
				Config.setDefaultSecret("nNDLeWFeoYtkqd5D");
				inputFileParam = new Param("File", Paths.get(inputFile));
				switch (ext.toLowerCase()) {

				case "doc":
				case "html":
				case "docx":
					ConvertApi.convert(ext, "pdf", inputFileParam).get()
							.saveFilesSync(Paths.get(Paths.get(inputFile).getParent().toString()));
					break;
				case "pdf":
					break;
				}
				DocumentToHtmlConverter.log.info("File converted to PDF successfully!");
				return outputFile;

			} catch (IOException | InterruptedException | ExecutionException e) {
				return inputFile;
			}
		} else {
			throw new BadFileFormatException("Verify the document format. We can only parse html, docx, pdf files!");
		}

	}

	public static String parseToHTMLUsingConvertApi(String inputFile) {
		String outputFile = FilenameUtils.removeExtension(inputFile) + ".html";

		try {
			Param inputFileParam = new Param("File", Paths.get(FilenameUtils.removeExtension(inputFile) + ".docx"));

			Config.setDefaultSecret("nNDLeWFeoYtkqd5D");

			ConvertApi.convert("docx", "html", inputFileParam).get()
					.saveFilesSync(Paths.get(Paths.get(inputFile).getParent().toString()));

			DocumentToHtmlConverter.log.info("File converted to PDF successfully!");
			return outputFile;

		} catch (IOException | InterruptedException | ExecutionException e) {
			return inputFile;
		}

	}

	public static String parseToPNGUsingPDFBox(String inputFile) {
		String ext = FilenameUtils.getExtension(inputFile);
		String outputFile = FilenameUtils.removeExtension(inputFile) + ".png";
		Param inputFileParam = null;

		if (Arrays.asList("docx", "doc", "pdf", "html").contains(ext.toLowerCase())) {
			try {
				Config.setDefaultSecret("nNDLeWFeoYtkqd5D");
				inputFileParam = new Param("File", Paths.get(inputFile));
				ConvertApi.convert(ext, "png", inputFileParam).get()
						.saveFilesSync(Paths.get(Paths.get(inputFile).getParent().toString()));

				DocumentToHtmlConverter.log.info("File converted to PDF successfully!");
				return outputFile;

			} catch (IOException | InterruptedException | ExecutionException e) {
				return inputFile;
			}
		}

		return inputFile;
	}

	/**
	 * parse document to html file
	 * 
	 * @param file
	 * @return html file
	 * @throws SAXException
	 */
	public static File parseToHTMLUsingApacheTikka(String file) throws SAXException {

		String ext = FilenameUtils.getExtension(file);
		String outputFileFormat = "";
		if (ext.equalsIgnoreCase("html") | ext.equalsIgnoreCase("pdf") | ext.equalsIgnoreCase("doc")
				| ext.equalsIgnoreCase("docx")) {
			outputFileFormat = ".html";
		} else if (ext.equalsIgnoreCase("txt") | ext.equalsIgnoreCase("rtf")) {
			outputFileFormat = ".txt";
		} else {
			DocumentToHtmlConverter.log.info("Input format of the file " + file + " is not supported.");
			return null;
		}
		String OUTPUT_FILE_NAME = FilenameUtils.removeExtension(file) + outputFileFormat;
		ToXMLContentHandler handler = new ToXMLContentHandler();
		AutoDetectParser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		try (InputStream stream = new FileInputStream(file)) {

			parser.parse(stream, handler, metadata);
			FileWriter htmlFileWriter = new FileWriter(OUTPUT_FILE_NAME);
			htmlFileWriter.write(handler.toString());
			htmlFileWriter.flush();
			htmlFileWriter.close();
			return new File(OUTPUT_FILE_NAME);
		} catch (IOException | TikaException e) {
			DocumentToHtmlConverter.log.error(e.getMessage());
		}
		return null;
	}
}
