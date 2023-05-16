package com.parser.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.parser.service.ParserService;
import com.parser.wrapper.ResponseWrapper;

import io.swagger.v3.oas.annotations.Operation;



@RestController
@CrossOrigin
public class ParserController {

	@Autowired
	private ParserService parserService;

	@Operation(summary = "parser file")
	@PostMapping(value="/upload", consumes = {
		      "multipart/form-data"
	   })
	public ResponseWrapper parseResume(@RequestParam MultipartFile resume) {
		ResponseWrapper responseWrapper = null;
		try {
			responseWrapper = parserService.parseResume(resume);
		} catch (Exception ex) {
			responseWrapper = new ResponseWrapper();
			responseWrapper.setMessage(ex.getMessage());
			responseWrapper.setStatus(500);
			ex.printStackTrace();
		}
		return responseWrapper;
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseWrapper handleMultipartException(Exception ex) {
		ResponseWrapper responseWrapper = new ResponseWrapper();
		responseWrapper.setData("No file uploaded");
		responseWrapper.setMessage("Please upload Resume!!");
		responseWrapper.setStatus(400);
		return responseWrapper;
	}

}
