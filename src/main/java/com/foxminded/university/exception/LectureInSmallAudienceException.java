package com.foxminded.university.exception;

import org.springframework.lang.Nullable;

public class LectureInSmallAudienceException extends ServiceLayerException {

	private static final long serialVersionUID = -4852735498788343348L;

	public LectureInSmallAudienceException(String errorMessage,  @Nullable Object... args) {
		super(errorMessage, args);
	}
}
