package com.parser.enums;

public enum RegEx {
	NAME("\\b([A-Z][a-zA-Z']+\\s*)+[A-Z][a-zA-Z']+\\s+\\b"),
	ADDRESS("\\b\\d+\\s+([a-zA-Z]+\\s*)+(\\,\\s*[a-zA-Z]+\\s*)+\\b"),
	LINK("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)"),
	EMAIL("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"),
	PHONE("(\\+\\s?\\d{1,3})?\\s*\\(?\\d{1,4}\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{2,9}"),
	EDUCATION(
			"\\b(FORMATION(S?)|Formation(s?)|(E|É)ducation(s?)|(E|É)DUCATION(S?)|ACADEMIC QUALIFICATION(S?)|Diploma(s?)|DIPLOMA(S?))\\b"),
	EXPERIENCE(
			"\\b(Project(s?)|Projet(s?)|Experience(s?)|EXPERIENCE(S?)|Work Experience|WORK EXPERIENCE|Work History|WORK HISTORY|Employment|EXPÉRIENCES P R O F E S S I O N N E L L E S)\\b"),
	SKILLS("\\b(Skill(s?) & Expertise(s?)|Tool(s?) & Technolog(y?|ies?)|Skill(s?)|SKILL(S?)|Technical Skills|TECHNICAL SKILLS|Computer Skill(s?)|COMPETENCE(S?)|COMPÉTENCE(S?)|Skills & Expertise|Expertise|AREAS OF EXPERTISE|COMPUTER SKILLS)\\b"),
	INTERESTS(
			"\\b(CENTRESD’INTÉRÊTS|INTEREST(S?)|Interest(s?)|Centre d'intérêt(s?)|CENTRE D'INTÉRÊT(S?)|INTÉRÊT(S?) ET LOISIR(S?)|Intérêt(s?) et loisir(s?))\\b"),
	ACTIVITIES(
			"\\b(Activity|Activities|ACTIVITY|ACTIVITIES|Activité(s?)|ACTIVITÉ(S?)|Activities and Societies|ACTIVITIES AND SOCIETIES)\\b"),
	OBJECTIVE("\\b(Objective(s?)|OBJECTIVE(S?)|Summary|SUMMARY|Statement)([^-!@#$%^&*()+.,?])\\b"), // summary included
																									// here
	LANGUAGES("\\b(LANGUE(S?)|Langue(s?)|Language(s?)|LANGUAGE(S?))\\b"),
	PERSONALPROJECTS("\\b(PROJECT(S?)|Project(s?)|PERSONAL PROJECTS|Personal Projects)\\b"),
	CERTIFICATIONS(
			"\\b(CERTIFICATION(S?)|CÉRTIFICAT(S?)|CERTIFICAT(S?)|Certification(s?)|Certificat(s?)|Cértificat(s?))\\b"),
	SCORES("\\b(TEST SCORE(S?)|SCORE(S?)|Scores(s?)|Test score(s?)|Test Score(s?))\\b"),
	MEMBERSHIP("\\b(Membership(s?)|MEMBERSHIP(S?))\\b"),
	ADDITIONAL("\\b(Award(s?)|AWARD(S)|Honor(s?)|HONOR(S?)|Accomplishment(s?)|ACCOMPLISHMENT(S?))\\b"),
	DATEFROMTO(
			"([A-Za-zÀ-ÿ,]+(\\s)*)([0-9]{4})(\\s)*(-|à|to)(\\s)*(\\b((P|p)resent|(C|c)urrent|(N|n)ow)\\b|([A-Za-zÀ-ÿ,]+(\\s)*([0-9]{0,4})))"),
	DATEFROMT1(
			"([0-9]{2}(\\s)*[A-Za-zÀ-ÿ,]+(\\s)*)([0-9]{0,4})(\\s)*(-|à|to)(\\s)*(\\b((P|p)resent|(C|c)urrent|(N|n)ow)\\b|([0-9]{2}(\\s)*[A-Za-zÀ-ÿ,]+(\\s)*([0-9]{0,4})))"),
	DATEFORMAT2("(\\d{2}/\\d{2})?(\\s)*(-|à|to)(\\s)*(\\b((P|p)resent|(C|c)urrent|(N|n)ow)\\b|(\\d{2}/\\d{2}))"),
	DATEFORMAT3("[0-9]{4}(\\s)*(-|à|to)(\\b((P|p)resent|(C|c)urrent|(N|n)ow)\\b|([0-9]{4}))"),
	ONLINE("\\b(Online|ONLINE)\\b"), REDUNDANT_TEXT("\\b(\\w+\\s\\w+)\\b(.*\\b\\1\\b)+"),
	INTERVENTION("\\b(NIVEAUX D’INTERVENTION)\\b");

	/**
	 * Note: - if you have a combination of words make sure you put them in the
	 * beginning of the list
	 */

	private final String name;

	RegEx(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
