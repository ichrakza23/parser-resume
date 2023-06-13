package com.parser.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.parser.enums.RegEx;
import com.parser.models.Experience;
import com.parser.models.Index;
import com.parser.models.Section;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author izarati
 *
 */
@Slf4j
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
	public static Pattern datePattern = Pattern.compile(RegEx.DATEFROMTO.toString() + "|" + RegEx.DATEFROMT1.toString()
	+ "|" + RegEx.DATEFORMAT2.toString() + "|" + RegEx.DATEFORMAT3.toString()+ "|" +RegEx.DATEFORMAT4.toString());
	
	public static Pattern activitiesPattern = Pattern.compile(RegEx.ACTIVITIES.toString());
	public static Pattern objectivePattern = Pattern.compile(RegEx.OBJECTIVE.toString());
	public static Pattern membershipPattern = Pattern.compile(RegEx.MEMBERSHIP.toString());
	public static Pattern onlinePattern = Pattern.compile(RegEx.ONLINE.toString());
	public static Pattern additionalPattern = Pattern.compile(RegEx.ADDITIONAL.toString());
	public static Pattern redundantPattern = Pattern.compile(RegEx.REDUNDANT_TEXT.toString());
	public static Pattern interventionPattern = Pattern.compile(RegEx.INTERVENTION.toString());
	public static Pattern voluntaryPattern = Pattern.compile(RegEx.VOLUNTARYWORK.toString());

	public static Pattern personalSkillsPattern = Pattern.compile(RegEx.PERSONALSKILLS.toString());
	public static Pattern personalSummaryPattern = Pattern.compile(RegEx.PERSONALSUMMARY.toString());
	public static Pattern qualitiesPattern = Pattern.compile(RegEx.QUALITIES.toString());

	public static String cleanRedundantInfo(String content) {
		return content.replaceAll(RegEx.REDUNDANT_TEXT.toString(), "$1");

	}

	/**
	 * 
	 * 
	 * @param content
	 * @param pattern
	 * @return an information based on pattern. For exemple: phone number, address,
	 *         name,...
	 */
	public static String extractInfo(String content, Pattern pattern) {
		Matcher patternMatcher = pattern.matcher(content);
		String info = "";
		if (patternMatcher.find()) {
			info = patternMatcher.group();
		}
		return info;
	}

	/**
	 * 
	 * @param cvText
	 * @param section
	 * @return a section. For exemple: experience, education, projects,...
	 */
	public static String extractSection(String cvText, String section) {
		Map<String, Section> orderedSections = getOrderedSectionsByIndex(cvText);

		Integer startIndex = orderedSections.get(section) != null ? orderedSections.get(section).getIndex() : -1;
		Section nextSection = new Section();
		try {
			nextSection = getnextSection(orderedSections, section);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer endIndex = nextSection != null ? nextSection.getIndex() : -1;
		if (startIndex != null && startIndex != -1 && endIndex == -1)
			return cvText.substring(startIndex, cvText.length() - 1);
		if (startIndex != null && startIndex != -1 && endIndex != -1 && endIndex > startIndex) {

			return cvText.substring(startIndex, endIndex - nextSection.getLabel().length());

		}

		return ""; // If the section is not found, return an empty string or handle it as needed
	}

	/**
	 * 
	 * @param cvText
	 * @return a map of different available sections ordered by startI index
	 */
	public static Map<String, Section> getOrderedSectionsByIndex(String cvText) {
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
		Matcher interventionMatcher = interventionPattern.matcher(cvText);
		Map<String, Matcher> matcherMap = new HashMap<String, Matcher>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put(RegEx.EXPERIENCE.name(), experienceMatcher);

				put(RegEx.EDUCATION.name(), educationMatcher);
				put(RegEx.INTERESTS.name(), hobbiesMatcher);

				put(RegEx.SKILLS.name(), skillsMatcher);
				put(RegEx.LANGUAGES.name(), languagesMatcher);
				put(RegEx.PERSONALPROJECTS.name(), personalProjectsMatcher);

				put(RegEx.CERTIFICATIONS.name(), certificationsMatcher);
				put(RegEx.SCORES.name(), scoresMatcher);
				put(RegEx.ACTIVITIES.name(), activitiesMatcher);

				put(RegEx.OBJECTIVE.name(), objectiveMatcher);

				put(RegEx.ADDITIONAL.name(), additionalMatcher);
				put(RegEx.ONLINE.name(), onlineMatcher);
				put(RegEx.MEMBERSHIP.name(), membershipMatcher);
				put(RegEx.INTERVENTION.name(), interventionMatcher);
				put(RegEx.PERSONALSKILLS.name(), personalSkillsPattern.matcher(cvText));
				put(RegEx.VOLUNTARYWORK.name(), voluntaryPattern.matcher(cvText));
				put(RegEx.PERSONALSUMMARY.name(), personalSummaryPattern.matcher(cvText));
				put(RegEx.QUALITIES.name(), qualitiesPattern.matcher(cvText));
			}
		};
		Map<String, Section> sectionsMap = new HashMap<String, Section>();

		matcherMap.entrySet().stream().forEach(entry -> {
			putIntoSectionsMapIfMatches(sectionsMap, entry.getValue(), entry.getKey());
		});
		Map<String, Section> sortedMap = sectionsMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.comparingInt(e -> e.getIndex())))
				.collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()),
						LinkedHashMap::putAll);
		return sortedMap;
	}

	private static Map<String, Section> putIntoSectionsMapIfMatches(Map<String, Section> sectionsMap, Matcher matcher,
			String name) {
		if (matcher.find()) {
			sectionsMap.put(name, new Section(name, matcher.group(), Integer.valueOf(matcher.end())));
		}
		return sectionsMap;
	}

	/**
	 * 
	 * @param sectionsMap
	 * @param currentKey
	 * @return the next section in order to know where the actual section finish
	 */
	private static Section getnextSection(Map<String, Section> sectionsMap, String currentKey) {

		boolean foundCurrentKey = false;
		for (Map.Entry<String, Section> entry : sectionsMap.entrySet()) {
			if (foundCurrentKey)
				return entry.getValue();

			if (entry.getKey().equals(currentKey))
				foundCurrentKey = true;

		}
		return null;
	}

	public static java.util.List<Experience> setExperiences(String experience) {
		Matcher patternMatcher = datePattern.matcher(experience);
		Index currentIndex;
		java.util.List<Experience> experiences = new ArrayList<Experience>();
		Map<Integer, Index> indexes = new HashMap<>();
		int i = 0;
		while (patternMatcher.find()) {
			patternMatcher.group();
			currentIndex = new Index(patternMatcher.start(), patternMatcher.end());
			indexes.put(i, currentIndex);
			i++;
			ExtractUtils.log.info(patternMatcher.group());

		}
		Integer startExp = 0;
		for (Map.Entry<Integer, Index> entry : indexes.entrySet()) {

			Experience exp = new Experience();
			Integer startNextIndex = indexes.get(entry.getKey() + 1) != null
					? indexes.get(entry.getKey() + 1).getStartIndex()
					: null;
			exp.setTitle(experience.substring(startExp, experience.indexOf("\r\n", entry.getValue().getEndIndex())));

			exp.setPeriod(experience.substring(entry.getValue().getStartIndex(), entry.getValue().getEndIndex()));
			if (startNextIndex != null) {
				if (experience.lastIndexOf(".\r\n", startNextIndex) != -1) {
					exp.setDescription(
							experience.substring(experience.indexOf("\r\n", entry.getValue().getEndIndex()) + 1,
									experience.lastIndexOf(".\r\n", startNextIndex)));
					startExp = experience.lastIndexOf(".\r\n", startNextIndex);

				} else {
					exp.setDescription(
							experience.substring(experience.indexOf("\r\n", entry.getValue().getEndIndex()) + 1,
									experience.lastIndexOf("\r\n", startNextIndex)));
					startExp = experience.lastIndexOf("\r\n", startNextIndex);

				}

			} else {
				exp.setDescription(experience.substring(experience.indexOf("\r\n", entry.getValue().getEndIndex()) + 1,
						experience.length()));
			}
			experiences.add(exp);

		}

		return experiences;
	}

}
