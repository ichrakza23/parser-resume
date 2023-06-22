package com.parser.service.extractor.educationsExtractor;

import java.util.List;

import com.parser.models.Education;

public interface EducationsExtractor {
    /**
     * split the education section into different parts of educations based on date
     * @param education: string
     * @return List<Education>
     */
	List<Education> extractEducationsByDate(String education);
}
