package com.parser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

    @RestController
	@RequestMapping("/api/book")
	public class BookController {


	    @GetMapping("/{id}")
	    @Operation(summary = "Get a book by its id")
	    @ApiResponses(value = {
	            @ApiResponse(responseCode = "200", description = "Successfully updated donor information"),
	            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
	            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
	            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found"),
	            @ApiResponse(responseCode = "500", description = "Application failed to process the request")
	    })
	    public void findById(@PathVariable long id) {
	        System.out.print("book");
	    }

	    
	
}
