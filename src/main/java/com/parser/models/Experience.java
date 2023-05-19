package com.parser.models;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Experience {
private String title;
private String company;
private LocalDate startDate;
private LocalDate endDate;

}
