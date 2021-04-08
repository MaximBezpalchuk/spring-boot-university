package com.foxminded.university.model;

public enum Degree {
	/** Docent */
	ASSISTANT("Assistant"),
	/** Professor */
	PROFESSOR("Professor"),
	/** Degree is not known, or not specified. */
	UNKNOWN("Unknown");

	private final String name;

	private Degree(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
