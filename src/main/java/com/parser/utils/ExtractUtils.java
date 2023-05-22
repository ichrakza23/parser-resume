package com.parser.utils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parser.enums.RegEx;
import com.parser.models.Section;

/**
 * 
 * @author izarati
 *
 */
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
	public static Pattern datePattern = Pattern.compile(RegEx.DATEFROMTO.toString()+"|"+RegEx.DATEFROMT1.toString()+"|"+RegEx.DATEFORMAT2.toString()+"|"+RegEx.DATEFORMAT3.toString() );
	public static Pattern activitiesPattern = Pattern.compile(RegEx.ACTIVITIES.toString());
	public static Pattern objectivePattern = Pattern.compile(RegEx.OBJECTIVE.toString());
	public static Pattern membershipPattern = Pattern.compile(RegEx.MEMBERSHIP.toString());
	public static Pattern onlinePattern = Pattern.compile(RegEx.ONLINE.toString());
	public static Pattern additionalPattern = Pattern.compile(RegEx.ADDITIONAL.toString());

	/**
	 * 
	 * 
	 * @param content
	 * @param pattern
	 * @return an information based on pattern. For exemple: phone number, address, name,...
	 */
	public static String extractInfo(String content, Pattern pattern) {
		Matcher patternMatcher = pattern.matcher(content);
		 String info="";
		if (patternMatcher.find()) {
		    info= patternMatcher.group();
		}
		return info;
	}

	/**
	 * 
	 * @param cvText
	 * @param section
	 * @return a section. For exemple:  experience, education, projects,...
	 */
    public static String extractSection(String cvText, String section ) {
		Map<String, Section> orderedSections = getOrderedSectionsByIndex(cvText);
        
		Integer startIndex = orderedSections.get(section)!=null?orderedSections.get(section).getIndex():-1;
        Section nextSection= new Section();
    	try {
    		nextSection= getnextSection(orderedSections, section );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Integer endIndex=nextSection!=null? nextSection.getIndex():-1;
        if(startIndex!=null && startIndex!=-1   && endIndex==-1 )
        	return cvText.substring(startIndex, cvText.length()-1);
	    if (startIndex!=null && startIndex != -1 && endIndex != -1 && endIndex > startIndex) {

	    	return cvText.substring(startIndex, endIndex-nextSection.getLabel().length());


	    }

	    return ""; // If the section is not found, return an empty string or handle it as needed
	}
    /**
     * 
     * @param cvText
     * @return a map of different available sections ordered by startI index
     */
    public static  Map<String, Section>  getOrderedSectionsByIndex(String cvText) {
    	Matcher experienceMatcher = experiencePattern.matcher(cvText);
    	Matcher educationMatcher = educationPattern.matcher(cvText);
    	Matcher hobbiesMatcher = hobbiesPattern.matcher(cvText);
    	Matcher skillsMatcher = skillsPattern.matcher(cvText);
    	Matcher languagesMatcher = languagesPattern.matcher(cvText);
    	Matcher personalProjectsMatcher = personalProjectsPattern.matcher(cvText);
    	Matcher activitiesMatcher = activitiesPattern.matcher(cvText);

    	Matcher certificationsMatcher = certificationsPattern.matcher(cvText);
    	Matcher scoresMatcher = scoresPattern.matcher(cvText);
    	Matcher objectiveMatcher = objectivePattern.matcher(cvText);
    	Matcher additionalMatcher = additionalPattern.matcher(cvText);
    	Matcher onlineMatcher = onlinePattern.matcher(cvText);
    	Matcher membershipMatcher = membershipPattern.matcher(cvText);


    	Map<String, Section> sectionsMap = new HashMap<String, Section>();
		if (experienceMatcher.find()) {
			sectionsMap.put(RegEx.EXPERIENCE.name(), new Section(RegEx.EXPERIENCE.name(),experienceMatcher.group(), Integer.valueOf(experienceMatcher.end())));
			}
		if (educationMatcher.find()) {
			sectionsMap.put(RegEx.EDUCATION.name(), new Section(RegEx.EDUCATION.name(),educationMatcher.group(), Integer.valueOf(educationMatcher.end())));
			}
		if (hobbiesMatcher.find()) {
			sectionsMap.put(RegEx.INTERESTS.name(), new Section(RegEx.INTERESTS.name(),hobbiesMatcher.group(), Integer.valueOf(hobbiesMatcher.end())));
		}
		if (skillsMatcher.find()) {
			sectionsMap.put(RegEx.SKILLS.name(), new Section(RegEx.SKILLS.name(),skillsMatcher.group(), Integer.valueOf(skillsMatcher.end())));
			}
		if (languagesMatcher.find()) {
			sectionsMap.put(RegEx.LANGUAGES.name(), new Section(RegEx.LANGUAGES.name(),languagesMatcher.group(), Integer.valueOf(languagesMatcher.end())));
		}
		if (personalProjectsMatcher.find()) {
			sectionsMap.put(RegEx.PERSONALPROJECTS.name(), new Section(RegEx.PERSONALPROJECTS.name(),personalProjectsMatcher.group(), Integer.valueOf(personalProjectsMatcher.end())));
		}
		if (certificationsMatcher.find()) {
			sectionsMap.put(RegEx.CERTIFICATIONS.name(), new Section(RegEx.CERTIFICATIONS.name(),certificationsMatcher.group(), Integer.valueOf(certificationsMatcher.end())));
		}
		if (scoresMatcher.find()) {
			sectionsMap.put(RegEx.SCORES.name(), new Section(RegEx.SCORES.name(),scoresMatcher.group(), Integer.valueOf(scoresMatcher.end())));
		}
		if (activitiesMatcher.find()) {
			sectionsMap.put(RegEx.ACTIVITIES.name(), new Section(RegEx.ACTIVITIES.name(),activitiesMatcher.group(), Integer.valueOf(activitiesMatcher.end())));
		}
		if (objectiveMatcher.find()) {
			sectionsMap.put(RegEx.OBJECTIVE.name(), new Section(RegEx.OBJECTIVE.name(),objectiveMatcher.group(), Integer.valueOf(objectiveMatcher.end())));
		}
		if (additionalMatcher.find()) {
			sectionsMap.put(RegEx.ADDITIONAL.name(), new Section(RegEx.ADDITIONAL.name(),additionalMatcher.group(), Integer.valueOf(additionalMatcher.end())));
		}
		if (onlineMatcher.find()) {
			sectionsMap.put(RegEx.ONLINE.name(), new Section(RegEx.ONLINE.name(),onlineMatcher.group(), Integer.valueOf(onlineMatcher.end())));
		}
		if (membershipMatcher.find()) {
			sectionsMap.put(RegEx.MEMBERSHIP.name(), new Section(RegEx.MEMBERSHIP.name(),membershipMatcher.group(), Integer.valueOf(membershipMatcher.end())));
		}
		 Map<String, Section> sortedMap =  sectionsMap.entrySet()
	                .stream()
	                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(e -> e.getIndex())))
	                .collect(
	                        LinkedHashMap::new,
	                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
	                        LinkedHashMap::putAll
	                );
    	return sortedMap;
    }
    /**
     * 
     * @param sectionsMap
     * @param currentKey
     * @return the next section in order to know where the actual section finish
     */
    private static Section getnextSection( Map<String, Section> sectionsMap, String currentKey ) {

    	boolean foundCurrentKey = false;
         for (Map.Entry<String, Section> entry : sectionsMap.entrySet()) {
             if (foundCurrentKey)
                     return entry.getValue();
            
            	
             
             if (entry.getKey().equals(currentKey)) 
                 foundCurrentKey = true;
             
         }
    	return null;
    }
   
}
