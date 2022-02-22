package com.foxminded.university.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foxminded.university.dao.mapper.LectureMapper;
import com.foxminded.university.dto.LectureDto;
import com.foxminded.university.model.*;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@DataSet(value = {"data.yml"}, cleanAfter = true)
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public class LectureSystemTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LectureMapper lectureMapper;

    @Test
    public void whenGetAllLectures_thenAllLecturesReturned() throws Exception {
        Lecture lecture1 = createLectureNoId();
        lecture1.setId(1);
        Lecture lecture2 = createLectureNoId();
        lecture2.setId(2);
        lecture2.setDate(LocalDate.of(2021, 1, 2));
        List<Lecture> lectures = Arrays.asList(lecture1, lecture2);
        List<LectureDto> lectureDtos = lectures.stream().map(lectureMapper::lectureToDto).collect(Collectors.toList());
        Page<LectureDto> pageDtos = new PageImpl<>(lectureDtos, PageRequest.of(0, 3), 2);

        mockMvc.perform(get("/api/lectures")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(pageDtos)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenGetOneLecture_thenOneLectureReturned() throws Exception {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);

        mockMvc.perform(get("/api/lectures/{id}", lecture.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(lectureDto)))
            .andExpect(status().isOk());
    }

    @Test
    public void whenSaveLecture_thenLectureSaved() throws Exception {
        Lecture lecture = createLectureNoId();
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);

        mockMvc.perform(post("/api/lectures")
            .content(objectMapper.writeValueAsString(lectureDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

    @Test
    public void whenEditLecture_thenLectureFound() throws Exception {
        Lecture lecture = createLectureNoId();
        lecture.setId(1);
        LectureDto lectureDto = lectureMapper.lectureToDto(lecture);

        mockMvc.perform(patch("/api/lectures/{id}", 1)
            .content(objectMapper.writeValueAsString(lectureDto))
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteLecture_thenLectureDeleted() throws Exception {
        mockMvc.perform(delete("/api/lectures/{id}", 1)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    private Lecture createLectureNoId() {
        Cathedra cathedra = Cathedra.builder().id(1).name("Fantastic Cathedra").build();
        Audience audience = Audience.builder().id(1).room(1).capacity(5).cathedra(cathedra).build();
        Group group = Group.builder().id(1).name("Killers").cathedra(cathedra).build();
        Subject subject = Subject.builder().id(1).name("Test Name").description("Desc").cathedra(cathedra).build();
        Teacher teacher = Teacher.builder()
            .id(1)
            .firstName("FirstName")
            .lastName("LastName")
            .phone("8888")
            .address("abc")
            .email("1@a.com")
            .gender(Gender.MALE)
            .postalCode("123")
            .education("high")
            .birthDate(LocalDate.of(1990, 1, 1))
            .cathedra(cathedra)
            .subjects(Arrays.asList(subject))
            .gender(Gender.MALE)
            .degree(Degree.PROFESSOR)
            .build();
        LectureTime time = LectureTime.builder()
            .id(1)
            .start(LocalTime.of(8, 0))
            .end(LocalTime.of(9, 0))
            .build();

        return Lecture.builder()
            .audience(audience)
            .cathedra(cathedra)
            .date(LocalDate.of(2021, 1, 1))
            .groups(Arrays.asList(group))
            .subject(subject)
            .teacher(teacher)
            .time(time)
            .build();
    }
}
