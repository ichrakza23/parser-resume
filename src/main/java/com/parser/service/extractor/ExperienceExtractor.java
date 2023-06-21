package com.parser.service.extractor;

import java.util.List;

import com.parser.models.Experience;

public interface ExperienceExtractor {

	/**
	 * 
	 * @param experience
	 * @return array of experiences based on dates detector
	 */
	List<Experience> extractExperienceByDate(String experience);

	/**
	 * 
	 * @param experience
	 * @return array of experiences. The split is based on searching key words. If
	 *         existing key words we will split experience section by these keys.
	 *         Otherwise we will split based on date
	 */
	List<Experience> extractExperience(String experience);

}
