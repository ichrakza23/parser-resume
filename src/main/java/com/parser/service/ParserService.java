package com.parser.service;

import org.springframework.web.multipart.MultipartFile;

import com.parser.wrapper.ResponseWrapper;


public interface ParserService {

	ResponseWrapper parseResume(MultipartFile file);

}