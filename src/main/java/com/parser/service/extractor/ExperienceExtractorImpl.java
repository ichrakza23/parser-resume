package com.parser.service.extractor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.parser.enums.ExperienceRegex;
import com.parser.enums.RegEx;
import com.parser.models.Experience;
import com.parser.models.Index;
import com.parser.models.Section;
import com.parser.utils.ExtractUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExperienceExtractorImpl implements ExperienceExtractor {
	public static Pattern datePattern = Pattern.compile(
			RegEx.DATEFROMTO.toString() + "|" + RegEx.DATEFROMT1.toString() + "|" + RegEx.DATEFORMAT2.toString() + "|"
					+ RegEx.DATEFORMAT3.toString() + "|" + RegEx.DATEFORMAT4.toString());
	public static Pattern projectPattern = Pattern.compile(ExperienceRegex.PROJECT.toString());
	public static Pattern contextePattern = Pattern.compile(ExperienceRegex.CONTEXTE.toString());
	public static Pattern tasksPattern = Pattern.compile(ExperienceRegex.ACHIEVEMENTS.toString());
	public static Pattern stackPattern = Pattern.compile(ExperienceRegex.STACK.toString());
	public static Pattern clientPattern = Pattern.compile(ExperienceRegex.CLIENT.toString());
	public static Pattern featuresPattern = Pattern.compile(ExperienceRegex.FEATURES.toString());
	public static Pattern projectContextPattern = Pattern.compile(ExperienceRegex.PROJECT_CONTEXTE.toString());
	public static Pattern rolePattern = Pattern.compile(ExperienceRegex.ROLE.toString());
	public static Pattern technicalSolutionsPattern = Pattern.compile(ExperienceRegex.TECHNICAL_SOLUTIONS.toString());

	@Override
	public List<Experience> extractExperienceByDate(String experience) {
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
			ExperienceExtractorImpl.log.info(patternMatcher.group());

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
					exp.setContext(experience.substring(experience.indexOf("\r\n", entry.getValue().getEndIndex()) + 1,
							experience.lastIndexOf(".\r\n", startNextIndex)));
					startExp = experience.lastIndexOf(".\r\n", startNextIndex);

				} else {
					exp.setContext(experience.substring(experience.indexOf("\r\n", entry.getValue().getEndIndex()) + 1,
							experience.lastIndexOf("\r\n", startNextIndex)));
					startExp = experience.lastIndexOf("\r\n", startNextIndex);

				}

			} else {
				exp.setContext(experience.substring(experience.indexOf("\r\n", entry.getValue().getEndIndex()) + 1,
						experience.length()));
			}
			experiences.add(exp);

		}

		return experiences;
	}

	@Override
	public List<Experience> extractExperience(String experience) {

		Matcher projectMatcher = projectPattern.matcher(experience);
		Matcher contexteMatcher = contextePattern.matcher(experience);
		Matcher featuresMatcher = featuresPattern.matcher(experience);
		Matcher clientMatcher = clientPattern.matcher(experience);
		Matcher tasksMatcher = tasksPattern.matcher(experience);
		Matcher stackMatcher = stackPattern.matcher(experience);
		Matcher projectContextMatcher = projectContextPattern.matcher(experience);
		Matcher roleMatcher = rolePattern.matcher(experience);
		Matcher technicalSolutionsMatcher = technicalSolutionsPattern.matcher(experience);

		Map<String, Matcher> matcherMap = new HashMap<String, Matcher>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put(ExperienceRegex.PROJECT.name(), projectMatcher);

				put(ExperienceRegex.CONTEXTE.name(), contexteMatcher);
				put(ExperienceRegex.PROJECT_CONTEXTE.name(), projectContextMatcher);
				put(ExperienceRegex.FEATURES.name(), featuresMatcher);

				put(ExperienceRegex.CLIENT.name(), clientMatcher);
				put(ExperienceRegex.ACHIEVEMENTS.name(), tasksMatcher);
				put(ExperienceRegex.STACK.name(), stackMatcher);
				put(ExperienceRegex.ROLE.name(), roleMatcher);
				put(ExperienceRegex.TECHNICAL_SOLUTIONS.name(), technicalSolutionsMatcher);

			}
		};
		AtomicReference<Map<String, Section>> sectionsMap = new AtomicReference<>(new HashMap<>());

		matcherMap.entrySet().stream().forEach(entry -> {
			ExtractUtils.putIntoSectionsMapIfMatches(sectionsMap.get(), entry.getValue(), entry.getKey());
		});
		if (sectionsMap != null && !sectionsMap.get().isEmpty()
				&& (sectionsMap.get().keySet().contains(ExperienceRegex.CONTEXTE.name())
						|| sectionsMap.get().keySet().contains(ExperienceRegex.PROJECT_CONTEXTE.name())
						|| sectionsMap.get().keySet().contains(ExperienceRegex.FEATURES.name())
						|| sectionsMap.get().keySet().contains(ExperienceRegex.CLIENT.name())
						|| sectionsMap.get().keySet().contains(ExperienceRegex.ACHIEVEMENTS.name())
						|| sectionsMap.get().keySet().contains(ExperienceRegex.ROLE.name()))) {

			return extractExperienceByKeys(sectionsMap, matcherMap, experience);
		} else {
			return extractExperienceByDate(experience);
		}

	}

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

	private List<Experience> extractExperienceByKeys(AtomicReference<Map<String, Section>> sectionsMap,
			Map<String, Matcher> matcherMap, String experience) {
		java.util.List<Experience> experiences = new ArrayList<Experience>();

		while (sectionsMap != null && !sectionsMap.get().isEmpty()) {

			Map<String, Section> sortedMap = sectionsMap.get().entrySet().stream()
					.sorted(Map.Entry.comparingByValue(Comparator.comparingInt(e -> e.getIndex())))
					.collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()),
							LinkedHashMap::putAll);

			Experience exp = new Experience();
			Map<String, Section> sectionMapToReplace = new HashMap<String, Section>();
			matcherMap.entrySet().stream().forEach(entryM -> {
				ExtractUtils.putIntoSectionsMapIfMatches(sectionMapToReplace, entryM.getValue(), entryM.getKey());
			});
			Map<String, Section> sortedCC = sectionMapToReplace.entrySet().stream()
					.sorted(Map.Entry.comparingByValue(Comparator.comparingInt(e -> e.getIndex())))
					.collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()),
							LinkedHashMap::putAll);
			sortedMap.entrySet().stream().forEach(entry -> {
				Integer startIndex = entry.getValue().getIndex();
				Section nextSection = new Section();

				try {
					nextSection = getnextSection(sortedMap, entry.getKey());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Integer endIndex = nextSection != null ? nextSection.getIndex() : -1;

				String part = "";

				if (startIndex != null && startIndex != -1 && endIndex == -1) {
					if (sortedCC == null || sortedCC.isEmpty()) {
						part = experience.substring(startIndex, experience.length() - 1);

					} else {
						Collection<Section> values = sortedCC.values();

						if (!values.isEmpty()) {
							Integer lI = values.iterator().next().getIndex();
							part = lI > startIndex ? experience.substring(startIndex, lI) : "";

						}
					}

				}
				if (startIndex != null && startIndex != -1 && endIndex != -1 && endIndex > startIndex) {

					part = experience.substring(startIndex, endIndex - nextSection.getLabel().length());

				}
				part = part.trim().replaceFirst("^.*?(?=[a-zA-ZÀ-ÖØ-öø-ÿ0-9 ])", "");

				if (entry.getValue() != null) {
					ExperienceRegex key = ExperienceRegex.valueOf(entry.getKey());
					switch (key) {
					case PROJECT:
						exp.setTitle(part);
						break;
					case CLIENT:
						exp.setClient(part);
						break;
					case PROJECT_CONTEXTE:
					case CONTEXTE:
						exp.setContext(exp.getContext() != null ? exp.getContext() + " " + part : part);
						break;
					case FEATURES:
					case ACHIEVEMENTS:
						String[] parts = part.split("\\r\\n");
						String[] cleanParts = Arrays.stream(parts).filter(element -> !element.isBlank())
								.toArray(String[]::new);
						if (exp.getMissions() != null) {
							String[] result = Arrays.copyOf(exp.getMissions(),
									exp.getMissions().length + cleanParts.length);
							System.arraycopy(cleanParts, 0, result, exp.getMissions().length, cleanParts.length);
							exp.setMissions(result);
						} else {
							exp.setMissions(cleanParts);

						}

						break;
					case STACK:
						exp.setKeyWords(part);
						break;
					case TECHNICAL_SOLUTIONS:
						if (exp.getKeyWords() == null || exp.getKeyWords().isBlank()) {
							exp.setKeyWords(part);
						}
						break;
					case ROLE:
						exp.setRole(part);
						break;
					default:
						break;

					}
				}

			});
			experiences.add(exp);
			sectionsMap.set(sectionMapToReplace);

		}
		return experiences;
	}

}
