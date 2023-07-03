package com.parser.service;

import org.springframework.web.multipart.MultipartFile;

import com.parser.wrapper.ResponseWrapper;

import exception.BadFileFormatException;

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
	 * @throws Exception 
	 */
	ResponseWrapper parseResume(MultipartFile file) throws BadFileFormatException;

}