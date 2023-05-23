package com.parser.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseWrapper {

	private Integer status;
	private Object data;
	private String message;

}
