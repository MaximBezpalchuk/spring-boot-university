package com.foxminded.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foxminded.university.dao.mapper.LectureToEventMapper;
import com.foxminded.university.model.Event;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LectureController {

    private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

    private final LectureToEventMapper lectureToEventMapper;
    private final LectureService lectureService;
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final AudienceService audienceService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final CathedraService cathedraService;
    private final LectureTimeService lectureTimeService;

    public LectureController(LectureService lectureService, GroupService groupService, TeacherService teacherService,
                             AudienceService audienceService, SubjectService subjectService, CathedraService cathedraService,
                             LectureTimeService lectureTimeService, StudentService studentService, LectureToEventMapper lectureToEventMapper) {
        this.lectureService = lectureService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.audienceService = audienceService;
        this.subjectService = subjectService;
        this.cathedraService = cathedraService;
        this.lectureTimeService = lectureTimeService;
        this.studentService = studentService;
        this.lectureToEventMapper = lectureToEventMapper;
    }

    @GetMapping("/lectures")
    public String all(Model model, Pageable pageable) {
        logger.debug("Show index page");
        Page<Lecture> page = lectureService.findAll(pageable);
        model.addAttribute("lectures", page);

        return "lectures/index";
    }

    @GetMapping("/lectures/{id}")
    public String showLecture(@PathVariable int id, Model model) {
        logger.debug("Show lecture page with id {}", id);
        model.addAttribute("lecture", lectureService.findById(id));

        return "lectures/show";
    }

    @GetMapping("/lectures/new")
    public String newLecture(Lecture lecture, Model model) {
        logger.debug("Show create page");
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());

        return "lectures/new";
    }

    @PostMapping("/lectures")
    public String create(@ModelAttribute Lecture lecture) {
        logger.debug("Create new lecture. Id {}", lecture.getId());
        lecture.setCathedra(cathedraService.findById(lecture.getCathedra().getId()));
        lecture.setTeacher(teacherService.findById(lecture.getTeacher().getId()));
        lecture.setAudience(audienceService.findById(lecture.getAudience().getId()));
        lecture.setTime(lectureTimeService.findById(lecture.getTime().getId()));
        lecture.setSubject(subjectService.findById(lecture.getSubject().getId()));
        lecture.setGroups(lecture.getGroups().stream().map(group -> groupService.findById(group.getId()))
            .collect(Collectors.toList()));
        lectureService.save(lecture);

        return "redirect:/lectures";
    }

    @GetMapping("/lectures/{id}/edit")
    public String editLecture(@PathVariable int id, Model model) {
        logger.debug("Show edit lecture page");
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("audiences", audienceService.findAll());
        model.addAttribute("times", lectureTimeService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("lecture", lectureService.findById(id));

        return "lectures/edit";
    }

    @PatchMapping("/lectures/{id}")
    public String update(@ModelAttribute Lecture lecture, @PathVariable int id) {
        logger.debug("Update lecture with id {}", id);
        lecture.setCathedra(cathedraService.findById(lecture.getCathedra().getId()));
        lecture.setTeacher(teacherService.findById(lectureService.findById(id).getTeacher().getId()));
        lecture.setAudience(audienceService.findById(lecture.getAudience().getId()));
        lecture.setTime(lectureTimeService.findById(lecture.getTime().getId()));
        lecture.setSubject(subjectService.findById(lecture.getSubject().getId()));
        lecture.setGroups(lecture.getGroups().stream().map(group -> groupService.findById(group.getId()))
            .collect(Collectors.toList()));
        lectureService.save(lecture);

        return "redirect:/lectures/" + id;
    }

    @DeleteMapping("/lectures/{id}")
    public String delete(@ModelAttribute Lecture lecture) {
        logger.debug("Delete lecture with id {}", lecture.getId());
        lectureService.delete(lecture.getId());

        return "redirect:/lectures";
    }

    @GetMapping("/teachers/{id}/shedule")
    public String choseTeachersPeriod(@ModelAttribute Event event, @PathVariable int id, Model model) {
        logger.debug("Chose period for calendar - teacher");
        model.addAttribute("teacher", teacherService.findById(id));

        return "teachers/calendar_chose_period";
    }

    @PostMapping("/teachers/{id}/shedule")
    public ModelAndView showTeachersShedule(@PathVariable int id, Model model, @RequestBody String body) {
        logger.debug("Show schedule for teacher");
        model.addAttribute("teacher", teacherService.findById(id));
        model.addAttribute("params", body);

        return new ModelAndView("teachers/calendar");
    }

    @GetMapping(value = "/teachers/{id}/shedule/events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getLecturesByTeacherIdAndPeriod(@PathVariable int id,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Show json lectures for teacher with id {} and period {} - {}", id, start, end);
        ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
        List<Event> events = lectureService.findByTeacherIdAndPeriod(id, start, end).stream()
            .map(lectureToEventMapper::lectureToEvent).collect(Collectors.toList());
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @GetMapping("/students/{id}/shedule")
    public String choseStudentsPeriod(@ModelAttribute Event event, @PathVariable int id, Model model) {
        logger.debug("Chose period for calendar - student");
        model.addAttribute("student", studentService.findById(id));

        return "students/calendar_chose_period";
    }

    @PostMapping("/students/{id}/shedule")
    public ModelAndView showStudentsShedule(@PathVariable int id, Model model, @RequestBody String body) {
        logger.debug("Show schedule for student");
        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("params", body);

        return new ModelAndView("students/calendar");
    }

    @GetMapping(value = "/students/{id}/shedule/events", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getLecturesByStudentIdAndPeriod(@PathVariable int id,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        logger.debug("Show json lectures for student with id {} and period {} - {}", id, start, end);
        ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();
        List<Event> events = lectureService.findByStudentIdAndPeriod(id, start, end).stream()
            .map(lectureToEventMapper::lectureToEvent).collect(Collectors.toList());
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    @GetMapping("/lectures/{id}/edit/teacher")
    public String editTeacher(@PathVariable int id, Model model) {
        logger.debug("Show edit teacher on lecture page");
        Lecture lecture = lectureService.findById(id);
        model.addAttribute("teachers", teacherService.findTeachersForChange(lecture));
        model.addAttribute("lecture", lecture);

        return "lectures/edit_teacher";
    }

    @PatchMapping("/lectures/{id}/edit/teacher")
    public String updateTeacher(@ModelAttribute Lecture lecture, @PathVariable int id) {
        logger.debug("Update lecture with id {}", id);
        Lecture actualLecture = lectureService.findById(id);
        lecture.setCathedra(actualLecture.getCathedra());
        lecture.setTeacher(teacherService.findById(lecture.getTeacher().getId()));
        lecture.setAudience(actualLecture.getAudience());
        lecture.setTime(actualLecture.getTime());
        lecture.setSubject(actualLecture.getSubject());
        lecture.setGroups(actualLecture.getGroups());
        lecture.setDate(actualLecture.getDate());
        lectureService.save(lecture);

        return "redirect:/lectures/" + id;
    }
}