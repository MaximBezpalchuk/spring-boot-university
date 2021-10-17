package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;

@ExtendWith(MockitoExtension.class)
public class LectureTimeControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
	private LectureTimeService lectureTimeService;
	@InjectMocks
	private LectureTimeController lectureTimeController;
	
	@BeforeEach
    public void setMocks() {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureTimeController).build();
    }
	
	@Test
    public void whenGetAllLectureTimes_thenAllLectureTimesReturned() throws Exception {
		LectureTime first = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 45))
				.build();
		LectureTime second = LectureTime.builder()
				.id(2)
				.start(LocalTime.of(10, 0))
				.end(LocalTime.of(12, 45))
				.build();
		
		List<LectureTime> list = Arrays.asList(first, second);
		
        when(lectureTimeService.findAll()).thenReturn(list);
 
        mockMvc.perform(get("/lecturetimes"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/index"))
                .andExpect(forwardedUrl("lecturetimes/index"))
                .andExpect(model().attribute("lectureTimes", hasSize(2)))
                .andExpect(model().attribute("lectureTimes", list));
 
        verify(lectureTimeService, times(1)).findAll();
        verifyNoMoreInteractions(lectureTimeService);
    }
	
	@Test
	void whenCreateNewLectureTime_thenLectureTimeCreated() throws Exception {		
		mockMvc.perform(get("/lecturetimes/new"))
				.andExpect(status().isOk())
				.andExpect(view().name("lecturetimes/new"))
				.andExpect(forwardedUrl("lecturetimes/new"))
				.andExpect(model().attribute("lectureTime", instanceOf(LectureTime.class)));
	}
	
	@Test
	void whenSaveLectureTime_thenLectureTimeSaved() throws Exception {
		LectureTime lectureTime = LectureTime.builder()
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 45))
				.build();
		mockMvc.perform(post("/lecturetimes").flashAttr("lectureTime", lectureTime))		
				.andExpect(redirectedUrl("/lecturetimes"));
		
		verify(lectureTimeService).save(lectureTime);
	}

	@Test
	void whenEditLectureTime_thenLectureTimeFound() throws Exception {
		LectureTime expected = LectureTime.builder()
				.id(1)
				.start(LocalTime.of(8, 0))
				.end(LocalTime.of(9, 45))
				.build();
		
		when(lectureTimeService.findById(1)).thenReturn(expected);
		
		mockMvc.perform(get("/lecturetimes/{id}/edit", 1))
				.andExpect(status().isOk())
				.andExpect(view().name("lecturetimes/edit"))
				.andExpect(forwardedUrl("lecturetimes/edit"))
				.andExpect(model().attribute("lectureTime", is(expected)));
	}
	
	@Test
	void whenDeleteLectureTime_thenLectureTimeDeleted() throws Exception {
		mockMvc.perform(delete("/lecturetimes/{id}", 1))
				.andExpect(redirectedUrl("/lecturetimes"));
		
		verify(lectureTimeService).deleteById(1);
	}
}