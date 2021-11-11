package com.foxminded.university.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.foxminded.university.dao.jdbc.mapper.LectureToEventMapper;
import com.foxminded.university.model.Event;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.model.Subject;

@ExtendWith(MockitoExtension.class)
public class LectureToEventMapperTest {

	@Test
	void shouldMapLectureToEvent() throws Exception {
		// given
		Subject subject = Subject.builder()
				.id(1)
				.name("Subject name")
				.build();
		LectureTime time = LectureTime.builder().id(1).start(LocalTime.of(8, 0)).end(LocalTime.of(9, 45)).build();
		Lecture lecture = Lecture.builder()
				.id(1)
				.date(LocalDate.of(2021, 1, 1))
				.subject(subject)
				.time(time)
				.build();
		// when
		Event event = LectureToEventMapper.INSTANCE.lectureToEvent(lecture);

		// then
		assertNotNull(event);
		assertEquals(event.getTitle(), "Subject name");
		assertEquals(event.getUrl(), "/university/lectures/1");
		assertEquals(event.getStart(), LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(8, 0)));
		assertEquals(event.getEnd(), LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(9, 45)));
	}

}
