package com.parser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
	@RequestMapping("/api/book")
	public class BookController {


	    @GetMapping("/{id}")
	    @Operation(summary = "Get a book by its id")

	    public void findById(@PathVariable long id) {
	        System.out.print("book");
	    }

	    
	
}
