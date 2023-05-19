package com.parser.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parser.enums.RegEx;
import com.parser.models.Experience;
import com.parser.models.Section;

import jakarta.validation.constraints.AssertFalse.List;

public class ExtractUtils {
	public static Pattern emailPattern = Pattern.compile(RegEx.EMAIL.toString());
	public static Pattern phonePattern = Pattern.compile(RegEx.PHONE.toString());
	public static Pattern addressPattern = Pattern.compile(RegEx.ADDRESS.toString());
	public static Pattern namePattern = Pattern.compile(RegEx.NAME.toString());
	public static Pattern experiencePattern = Pattern.compile(RegEx.EXPERIENCE.toString());
	public static Pattern educationPattern = Pattern.compile(RegEx.EDUCATION.toString());
	public static Pattern hobbiesPattern = Pattern.compile(RegEx.INTERESTS.toString());
	public static Pattern skillsPattern = Pattern.compile(RegEx.SKILLS.toString());
	public static Pattern languagesPattern = Pattern.compile(RegEx.LANGUAGES.toString());
	public static Pattern personalProjectsPattern = Pattern.compile(RegEx.PERSONALPROJECTS.toString());
	public static Pattern certificationsPattern = Pattern.compile(RegEx.CERTIFICATIONS.toString());
	public static Pattern scoresPattern = Pattern.compile(RegEx.SCORES.toString());
	public static Pattern datePattern = Pattern.compile(RegEx.DATEFROMTO.toString());

	public static String extractInfo(String content, Pattern pattern) {
		Matcher patternMatcher = pattern.matcher(content);
		 String info="";
		if (patternMatcher.find()) {
		    info= patternMatcher.group();
		}
		return info;
	}

    public static String extractSection(String cvText, String section ) {
		Map<String, Integer> orderedSections = getOrderedSectionsByIndex(cvText);
        Integer startIndex = orderedSections.get(section);
        Section nextSection= new Section();
    	try {
    		nextSection= getnextSection(orderedSections, section );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Integer endIndex=nextSection!=null? nextSection.getIndex():-1;
        if(startIndex!=null &&startIndex!=-1   &&endIndex==-1 )
        	return cvText.substring(startIndex, cvText.length()-1);
	    if (startIndex!=null && startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
	    	String sectionContent = cvText.substring(startIndex, endIndex);
	    	int lastInline = sectionContent.lastIndexOf("\n");
		if(lastInline!=-1) 
			return sectionContent.substring(0, lastInline);
		
		return sectionContent;

	    }

	    return ""; // If the section is not found, return an empty string or handle it as needed
	}
    public static  Map<String, Integer>  getOrderedSectionsByIndex(String cvText) {
    	Matcher experienceMatcher = experiencePattern.matcher(cvText);
    	Matcher educationMatcher = educationPattern.matcher(cvText);
    	Matcher hobbiesMatcher = hobbiesPattern.matcher(cvText);
    	Matcher skillsMatcher = skillsPattern.matcher(cvText);
    	Matcher languagesMatcher = languagesPattern.matcher(cvText);
    	Matcher personalProjectsMatcher = personalProjectsPattern.matcher(cvText);

    	Matcher certificationsMatcher = certificationsPattern.matcher(cvText);
    	Matcher scoresMatcher = scoresPattern.matcher(cvText);


    	Map<String, Integer> sectionsMap = new HashMap<String, Integer>();
		if (experienceMatcher.find()) {
			sectionsMap.put(RegEx.EXPERIENCE.name(), Integer.valueOf(experienceMatcher.end()));
			}
		if (educationMatcher.find()) {
			sectionsMap.put(RegEx.EDUCATION.name(), Integer.valueOf(educationMatcher.end()));
			}
		if (hobbiesMatcher.find()) {
			sectionsMap.put(RegEx.INTERESTS.name(), Integer.valueOf(hobbiesMatcher.end()));
		}
		if (skillsMatcher.find()) {
			sectionsMap.put(RegEx.SKILLS.name(), Integer.valueOf(skillsMatcher.end()));
			}
		if (languagesMatcher.find()) {
			sectionsMap.put(RegEx.LANGUAGES.name(), Integer.valueOf(languagesMatcher.end()));
		}
		if (personalProjectsMatcher.find()) {
			sectionsMap.put(RegEx.PERSONALPROJECTS.name(), Integer.valueOf(personalProjectsMatcher.end()));
		}
		if (certificationsMatcher.find()) {
			sectionsMap.put(RegEx.CERTIFICATIONS.name(), Integer.valueOf(certificationsMatcher.end()));
		}
		if (scoresMatcher.find()) {
			sectionsMap.put(RegEx.SCORES.name(), Integer.valueOf(scoresMatcher.end()));
		}
		 Map<String, Integer> sortedMap =  sectionsMap.entrySet()
	                .stream()
	                .sorted(Map.Entry.comparingByValue())
	                .collect(
	                        LinkedHashMap::new,
	                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
	                        LinkedHashMap::putAll
	                );
    	return sortedMap;
    }
    private static Section getnextSection( Map<String, Integer> sectionsMap, String currentKey ) {

    	boolean foundCurrentKey = false;
         for (Map.Entry<String, Integer> entry : sectionsMap.entrySet()) {
             if (foundCurrentKey)
             {
             	Section nextSection = new Section();

            	 nextSection.setName(entry.getKey());
                 nextSection.setIndex(entry.getValue());
                     return nextSection;
             }
            	
             
             if (entry.getKey().equals(currentKey)) 
                 foundCurrentKey = true;
             
         }
    	return null;
    }
   
}
