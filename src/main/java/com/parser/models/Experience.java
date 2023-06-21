package com.parser.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Experience {
	private String title;
	private String context;
	private String client;
	private String role;
	private String keyWords;
	private String[] missions;
	private String period;
	

}
