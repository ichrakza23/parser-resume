package com.parser.controller;

import java.nio.file.NoSuchFileException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.parser.service.ParserService;
import com.parser.wrapper.ResponseWrapper;

import exception.BadFileFormatException;
import exception.FileHandlerException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/parser")
public class ParserController {

	@Autowired
	private ParserService parserService;

	@Operation(summary = "parser file")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successfully parsed a file"),
			@ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
			@ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
			@ApiResponse(responseCode = "415", description = "Format document is not supported!"),
			@ApiResponse(responseCode = "500", description = "file parsing failure") })
	@PostMapping(value = "/upload", consumes = { "multipart/form-data" })
	public ResponseWrapper parseResume(@RequestParam MultipartFile resume)
			throws NoSuchFileException, BadFileFormatException, FileHandlerException {
		return parserService.parseResume(resume);
	}

	@ExceptionHandler(value = { NoSuchFileException.class, BadFileFormatException.class, FileHandlerException.class })
	public ResponseWrapper handleMultipartException(Exception ex) {
		ResponseWrapper responseWrapper = new ResponseWrapper();
		if (ex instanceof BadFileFormatException) {
			responseWrapper.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
			responseWrapper.setMessage("bad document format! " + ex);
		} else if (ex instanceof FileHandlerException) {
			responseWrapper.setStatus(500);
			responseWrapper.setMessage("An error when trying to treat the file! " + ex);
		} else if (ex instanceof NoSuchFileException) {
			responseWrapper.setStatus(400);
			responseWrapper.setMessage("No file uploaded, Please upload Resume!!");
		}
		return responseWrapper;
	}

}
