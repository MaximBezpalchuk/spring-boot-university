package com.foxminded.university.model;

public enum Gender {
	/** Male. */
	M("Male"),
	/** Female. */
	F("Female"),
	/** Gender is not known, or not specified. */
	UNKNOWN("Unknown");

	private final String name;

	private Gender(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
