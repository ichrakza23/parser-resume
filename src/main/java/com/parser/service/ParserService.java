package com.parser.service;

import java.nio.file.NoSuchFileException;

import org.springframework.web.multipart.MultipartFile;

import com.parser.wrapper.ResponseWrapper;

import exception.BadFileFormatException;
import exception.FileHandlerException;

/**
 * 
 * @author izarati
 *
 */
public interface ParserService {

	/**
	 * extract informations from the file
	 * @param file
	 * @return ResponseWrapper: different parts of the file
	 * @throws NoSuchFileException 
	 * @throws FileHandlerException 
	 * @throws Exception 
	 */
	ResponseWrapper parseResume(MultipartFile file) throws BadFileFormatException, NoSuchFileException, FileHandlerException;

}