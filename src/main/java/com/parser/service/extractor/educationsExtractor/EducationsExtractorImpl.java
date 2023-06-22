package com.parser.service.extractor.educationsExtractor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.parser.enums.RegEx;
import com.parser.models.Education;
import com.parser.models.Index;

@Service
public class EducationsExtractorImpl implements EducationsExtractor {
	public static Pattern datePattern = Pattern.compile(
			RegEx.DATEFROMTO.toString() + "|" + RegEx.DATEFROMT1.toString() + "|" + RegEx.DATEFORMAT2.toString() + "|"
					+ RegEx.DATEFORMAT3.toString() + "|" + RegEx.DATEFORMAT4.toString()+"|"+RegEx.YearFormat.toString()+ "|" + RegEx.MonthYearFormat.toString());

	@Override
	public List<Education> extractEducationsByDate(String education) {
		// clean education section
		education = education.trim().replaceFirst("^.*?(?=[a-zA-ZÀ-ÖØ-öø-ÿ0-9-/ ])", "");
		Matcher patternMatcher = datePattern.matcher(education);
		Index currentIndex;
		java.util.List<Education> educationParts = new ArrayList<Education>();
		Map<Integer, Index> indexes = new HashMap<>();
		int i = 0;
		boolean isEndPositionText = false;
		while (patternMatcher.find() ) {
			patternMatcher.group();
			// verify if date in the begin of part or last position
			if(i==0) {
				if (patternMatcher.start()==0) isEndPositionText=true;
			}
			currentIndex = new Index(patternMatcher.start(), patternMatcher.end());
			indexes.put(i, currentIndex);
			i++;

		}
		int startIndex=0;
		for (Map.Entry<Integer, Index> entry : indexes.entrySet()) {

			Education partEducation = new Education();
			Integer startNextIndex = indexes.get(entry.getKey() + 1) != null
					? indexes.get(entry.getKey() + 1).getStartIndex()
					: null;
			
			partEducation
					.setPeriod(education.substring(entry.getValue().getStartIndex(), entry.getValue().getEndIndex()));
			if (startNextIndex != null) {
				if(isEndPositionText) {
					partEducation.setQualification(
							education.substring( entry.getValue().getEndIndex(),startNextIndex ));
				} else {
					partEducation.setQualification(
							education.substring( startIndex,entry.getValue().getStartIndex() -1 ));
					startIndex= entry.getValue().getEndIndex()+1;
				}
				

			} else {
				if (isEndPositionText) {
					partEducation.setQualification(education.substring(entry.getValue().getEndIndex(), education.length()));
				}else {
					partEducation.setQualification(education.substring(startIndex, entry.getValue().getStartIndex()-1));
					}
			}
			
			educationParts.add(partEducation);

		}

		return educationParts;
	}

}
