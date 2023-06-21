package com.parser.enums;

public enum ExperienceRegex {
	PROJECT("\\b(Project|Projet)\\b"),
	PROJECT_CONTEXTE("\\b((C|c)ontexte du projet|D(é|e)scription)\\b"),
	CONTEXTE("\\b(Contexte\\s+:)\\b"),
	FEATURES("\\b(Fonctionnalité(s?)|Feature(s?)|Modules et fonctionnalités)\\b"),
	ACHIEVEMENTS("\\b(Réalisations|(T|t)ravail réalisé|(A|a)chievements|(T|t)asks|(T|t)âches réalisés)\\b"),
	TECHNICAL_SOLUTIONS("\\b(Solutions techniques)\\b"),
	STACK("\\b(Technologies|Mots clés|Key words|Keywords)\\b"),
	ROLE("\\b(Poste|Role)\\b"),
	CLIENT("\\b(Client|Company|Société|Entreprise)\\b");
	private final String name;

	ExperienceRegex(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
