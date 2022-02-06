package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class LectureTimeRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private LectureTimeService lectureTimeService;
    @InjectMocks
    private LectureTimeRestController lectureTimeRestController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lectureTimeRestController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    public void whenGetAllLectureTimes_thenAllLectureTimesReturned() throws Exception {
        LectureTime lectureTime1 = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 45))
            .build();
        LectureTime lectureTime2 = LectureTime.builder()
            .id(2)
            .start(LocalTime.of(10, 0))
            .end(LocalTime.of(12, 45))
            .build();
        List<LectureTime> lectureTimes = Arrays.asList(lectureTime1, lectureTime2);
        when(lectureTimeService.findAll()).thenReturn(lectureTimes);

        mockMvc.perform(get("/api/lecturetimes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].start", is("08:00")))
            .andExpect(jsonPath("$[0].end", is("09:45")));

        verifyNoMoreInteractions(lectureTimeService);
    }

    @Test
    public void whenGetOneLectureTime_thenOneLectureTimeReturned() throws Exception {
        LectureTime lectureTime = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 45))
            .build();
        when(lectureTimeService.findById(lectureTime.getId())).thenReturn(lectureTime);

        mockMvc.perform(get("/api/lecturetimes/{id}", lectureTime.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.start", is("08:00")))
            .andExpect(jsonPath("$.end", is("09:45")));
        ;
    }

    @Test
    void whenSaveLectureTime_thenLectureTimeSaved() throws Exception {
        LectureTime lectureTime = LectureTime.builder()
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 45))
            .build();
        mockMvc.perform(post("/api/lecturetimes")
            .content(objectMapper.writeValueAsString(lectureTime))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated());

        verify(lectureTimeService).save(lectureTime);
    }

    @Test
    void whenEditLectureTime_thenLectureTimeFound() throws Exception {
        LectureTime lectureTime = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 45))
            .build();

        mockMvc.perform(patch("/api/lecturetimes/{id}", 1)
            .content(objectMapper.writeValueAsString(lectureTime))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void whenDeleteLectureTime_thenLectureTimeDeleted() throws Exception {
        LectureTime lectureTime = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 45))
            .build();
        mockMvc.perform(delete("/api/lecturetimes/{id}", 1)
            .content(objectMapper.writeValueAsString(lectureTime))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(lectureTimeService).delete(lectureTime);
    }
}