package com.foxminded.university.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}