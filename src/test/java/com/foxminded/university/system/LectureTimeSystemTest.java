package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureTimeMapper;
import com.foxminded.university.dto.LectureTimeDto;
import com.foxminded.university.dto.Slice;
import com.foxminded.university.model.LectureTime;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class LectureTimeSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private LectureTimeMapper lectureTimeMapper = Mappers.getMapper(LectureTimeMapper.class);

    @Test
    public void whenGetAllLectureTimes_thenAllLectureTimesReturned() throws Exception {
        LectureTime lectureTime1 = createLectureTimeNoId();
        lectureTime1.setId(1);
        LectureTime lectureTime2 = createLectureTimeNoId();
        lectureTime2.setId(2);
        List<LectureTime> lectureTimes = Arrays.asList(lectureTime1, lectureTime2);
        List<LectureTimeDto> lectureTimeDtos = lectureTimes.stream().map(lectureTimeMapper::lectureTimeToDto).collect(Collectors.toList());

        mockMvc.perform(get("/api/lecturetimes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(new Slice(lectureTimeDtos))))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneLectureTime_thenOneLectureTimeReturned() throws Exception {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setId(1);
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);

        mockMvc.perform(get("/api/lecturetimes/{id}", lectureTime.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(lectureTimeDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenSaveLectureTime_thenLectureTimeSaved() throws Exception {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setStart(LocalTime.of(7, 0));
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);

        mockMvc.perform(post("/api/lecturetimes")
                .content(objectMapper.writeValueAsString(lectureTimeDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/api/lecturetimes/9"))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenEditLectureTime_thenLectureTimeFound() throws Exception {
        LectureTime lectureTime = createLectureTimeNoId();
        lectureTime.setId(1);
        lectureTime.setStart(LocalTime.of(7, 0));
        LectureTimeDto lectureTimeDto = lectureTimeMapper.lectureTimeToDto(lectureTime);

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
    }

    private LectureTime createLectureTimeNoId() {
        return LectureTime.builder()
                .start(LocalTime.of(8, 0))
                .end(LocalTime.of(9, 0))
                .build();
    }
}
