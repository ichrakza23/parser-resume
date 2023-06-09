package com.parser.enums;

public enum ExperienceRegex {
	PROJECT("\\b(Project|Projet)\\b"),
	PROJECT_CONTEXTE("\\b((C|c)ontexte du projet|D(é|e)scription)\\b"),
	CONTEXTE("Contexte :"),
	FEATURES("\\b(Fonctionnalité(s?)|Feature(s?)|Modules et fonctionnalités)\\b"),
	ACHIEVEMENTS("\\b(Réalisations|Réalisation :|(T|t)ravail réalisé|(A|a)chievements|(T|t)asks|(T|t)âches réalisés|Tâches)\\b"),
	MISSIONS_WITH_NO_KEYS("(•|-) (.+)"),
	TECHNICAL_SOLUTIONS("\\b(Solutions techniques)\\b"),
	STACK("\\b(Technologies|Mots clés|Key words|Keywords|Environnement technique)\\b"),
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
