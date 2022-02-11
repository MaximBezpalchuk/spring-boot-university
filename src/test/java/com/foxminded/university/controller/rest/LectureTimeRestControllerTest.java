package com.foxminded.university.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureTimeMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LectureTimeRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    @Spy
    private LectureTimeMapper lectureTimeMapper = Mappers.getMapper(LectureTimeMapper.class);
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
        LectureTime lectureTime1 = createLectureTimeNoId();
        lectureTime1.setId(1);
        LectureTime lectureTime2 = createLectureTimeNoId();
        lectureTime2.setId(2);
        List<LectureTime> lectureTimes = Arrays.asList(lectureTime1, lectureTime2);
        List<LectureTimeDto> lectureTimeDtos = lectureTimes.stream().map(lectureTimeMapper::lectureTimeToDto).collect(Collectors.toList());
        when(lectureTimeService.findAll()).thenReturn(lectureTimes);

        mockMvc.perform(get("/api/lecturetimes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(new Slice(lectureTimeDtos))))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(lectureTimeService);
    }

    @Test
    public void whenGetOneLectureTime_thenOneLectureTimeReturned() throws Exception {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setId(1);
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);
        when(lectureTimeService.findById(lectureTime.getId())).thenReturn(lectureTime);

        mockMvc.perform(get("/api/lecturetimes/{id}", lectureTime.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(lectureTimeDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveLectureTime_thenLectureTimeSaved() throws Exception {
        LectureTime lectureTime = createLectureTimeNoId();
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);
        when(lectureTimeService.save(lectureTime)).thenAnswer(I -> {
            lectureTime.setId(2);
            return lectureTime;
        });
        mockMvc.perform(post("/api/lecturetimes")
                .content(objectMapper.writeValueAsString(lectureTimeDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/lecturetimes/2"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditLectureTime_thenLectureTimeFound() throws Exception {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setId(1);
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);
        when(lectureTimeService.save(lectureTime)).thenReturn(lectureTime);

        mockMvc.perform(patch("/api/lecturetimes/{id}", 1)
                .content(objectMapper.writeValueAsString(lectureTimeDto))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteLectureTime_thenLectureTimeDeleted() throws Exception {
        mockMvc.perform(delete("/api/lecturetimes/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lectureTimeService).delete(1);
    }

    private LectureTime createLectureTimeNoId() {
        return LectureTime.builder()
                .start(LocalTime.of(8, 0))
                .end(LocalTime.of(9, 45))
                .build();
    }
}