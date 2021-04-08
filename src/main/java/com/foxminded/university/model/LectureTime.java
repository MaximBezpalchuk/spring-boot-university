package com.foxminded.university.model;

import java.time.LocalTime;

public class LectureTime {

	private LocalTime start;
	private LocalTime end;

	public LectureTime(LocalTime start, LocalTime end) {
		this.start = start;
		this.end = end;
	}

	public LocalTime getStart() {
		return start;
	}

	public LocalTime getEnd() {
		return end;
	}
}
