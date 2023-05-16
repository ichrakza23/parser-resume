package com.parser.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractUtils {
	static Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
	static Pattern phonePattern = Pattern.compile("(\\+\\d{1,3})?\\s*\\(?\\d{1,4}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{2,9}");
//	static Pattern addressPattern = Pattern.compile("^(\\d+\\s+)?\\b[a-zA-Z0-9]+\\b(\\s+\\b[a-zA-Z0-9]+\\b)*\\s*,\\s*\\b[a-zA-Z]+\\b(\\s+\\b[a-zA-Z]+\\b)*\\s*,\\s*\\b[a-zA-Z0-9]+\\b(\\s+\\b[a-zA-Z0-9]+\\b)*\\s*\\d*$");
//	static Pattern namePattern = Pattern.compile("([A-Z][a-z]+)\\s+([A-Z][a-z]+)");
	static Pattern addressPattern = Pattern.compile("\\b\\d+\\s+([a-zA-Z]+\\s*)+(\\,\\s*[a-zA-Z]+\\s*)+\\b");

	static Pattern namePattern = Pattern.compile("\\b[A-Z][a-zA-Z']+\\b");

//	public static Pattern experiencePattern = Pattern.compile("WORK EXPERIENCE|EXPERIENCE|Experience|Work History|Employment");
	public static Pattern experiencePattern = Pattern.compile("\\b(Experience(s?)|EXPERIENCE(S?)|Work Experience|WORK EXPERIENCE|Work History|WORK HISTORY)\\b");
	public static Pattern educationPattern = Pattern.compile("EDUCATION|ACADEMIC QUALIFICATIONS");
	public static Pattern hobbiesPattern = Pattern.compile("CENTRESD’INTÉRÊTS|INTERESTS");
	public static Pattern skillsPattern = Pattern.compile("COMPETENCES|SKILLS|COMPÉTENCES|Skills & Expertise|Expertise|AREAS OF EXPERTISE");
	public static Pattern languagesPattern = Pattern.compile("LANGUES|LANGUAGES");
	public static Pattern personalProjectsPattern = Pattern.compile("PROJECTS|PERSONAL PROJECTS");
	public static Pattern certificationsPattern = Pattern.compile("CERTIFICATIONS|CÉRTIFICATS|CERTIFICATS");
	public static Pattern scoresPattern = Pattern.compile("TEST SCORES|SCORES");

	public static String extractEmail(String content) {
		Matcher emailMatcher = emailPattern.matcher(content);
		 String email="";
		if (emailMatcher.find()) {
		    email= emailMatcher.group();
		}
		return email;
	}
	
	public static String extractPhone(String content) {
		Matcher phoneMatcher = phonePattern.matcher(content);
		 String phone="";
		if (phoneMatcher.find()) {
			phone= phoneMatcher.group();
		}
		return phone;
	}
	public static String extractName(String content) {
		Matcher nameMatcher = namePattern.matcher(content);
		 String name="";
		if (nameMatcher.find()) {
			    name=nameMatcher.group();
			    }
		return name;
	}
	public static String extractAddress(String content) {
		Matcher addressMatcher = addressPattern.matcher(content);
		 String address="";
		if (addressMatcher.find()) {
			address=addressMatcher.group();
			    }
		return address;
	}
    public static String extractSection(String cvText, Pattern startPattern, Pattern endPattern) {
//	    int startIndex = cvText.toUpperCase().indexOf(startPattern.toUpperCase());
//	    int endIndex = cvText.toUpperCase().indexOf(endPattern.toUpperCase(), startIndex + startPattern.length());
//	    int endIndex = cvText.toUpperCase().indexOf("CENTRESD’INTÉRÊTS".toUpperCase());
    	Matcher startMatcher = startPattern.matcher(cvText);
    	int startIndex=-1;
    	int endIndex=-1;
		if (startMatcher.find()) {
			startIndex=startMatcher.end();
			}
		if(startIndex==-1) return "";
       if(startIndex!=-1 &&endPattern==null )
    	   return cvText.substring(startIndex, cvText.length()-1);
		Matcher endMatcher = endPattern.matcher(cvText);
		if (endMatcher.find()) {
		     endIndex = endMatcher.end();
		}
	    if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
	        return cvText.substring(startIndex, endIndex-endMatcher.group().length());
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
			sectionsMap.put("Experience", Integer.valueOf(experienceMatcher.end()));
			}
		if (educationMatcher.find()) {
			sectionsMap.put("Education", Integer.valueOf(educationMatcher.end()));
			}
		if (hobbiesMatcher.find()) {
			sectionsMap.put("Hobbies", Integer.valueOf(hobbiesMatcher.end()));
		}
		if (skillsMatcher.find()) {
			sectionsMap.put("Skills", Integer.valueOf(skillsMatcher.end()));
			}
		if (languagesMatcher.find()) {
			sectionsMap.put("Languages", Integer.valueOf(languagesMatcher.end()));
		}
		if (personalProjectsMatcher.find()) {
			sectionsMap.put("PersonalProjects", Integer.valueOf(personalProjectsMatcher.end()));
		}
		if (certificationsMatcher.find()) {
			sectionsMap.put("Certifications", Integer.valueOf(certificationsMatcher.end()));
		}
		if (scoresMatcher.find()) {
			sectionsMap.put("Scores", Integer.valueOf(scoresMatcher.end()));
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
    public static Pattern getnextPattern(String currentKey, Map<String, Integer> sectionsMap ) {
        Map<String, Pattern> sectionsPattern= new HashMap<>();
        sectionsPattern.put("Education", educationPattern);
        sectionsPattern.put("Experience", experiencePattern);
        sectionsPattern.put("Hobbies", hobbiesPattern);
        sectionsPattern.put("Languages", languagesPattern);
        sectionsPattern.put("Skills", skillsPattern);
        sectionsPattern.put("Scores", scoresPattern);
        sectionsPattern.put("PersonalProjects", personalProjectsPattern);
        sectionsPattern.put("Certifications", certificationsPattern);


    	boolean foundCurrentKey = false;
         for (Map.Entry<String, Integer> entry : sectionsMap.entrySet()) {
             if (foundCurrentKey) 
                 return sectionsPattern.get(entry.getKey());
             
             if (entry.getKey().equals(currentKey)) 
                 foundCurrentKey = true;
             
         }
    	return null;
    }
}
