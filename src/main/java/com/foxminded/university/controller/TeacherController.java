package com.foxminded.university.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Teacher;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	private TeacherService teacherService;
	private SubjectService subjectService;
	private CathedraService cathedraService;
	private LectureService lectureService;

	public TeacherController(TeacherService teacherService, SubjectService subjectService,
			CathedraService cathedraService, LectureService lectureService) {
		this.teacherService = teacherService;
		this.subjectService = subjectService;
		this.cathedraService = cathedraService;
		this.lectureService = lectureService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Teacher> page = teacherService.findAll(pageable);
		model.addAttribute("teachers", page);

		return "teachers/index";
	}

	@GetMapping("/{id}")
	public String showTeacher(@PathVariable int id, Model model) {
		logger.debug("Show teacher page with id {}", id);
		model.addAttribute("teacher", teacherService.findById(id));

		return "teachers/show";
	}

	@GetMapping("/new")
	public String newStudent(Teacher teacher, Model model) {
		logger.debug("Show create page");
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());

		return "teachers/new";
	}

	@PostMapping
	public String create(@ModelAttribute Teacher teacher, Model model) {
		teacher.setCathedra(cathedraService.findById(teacher.getCathedra().getId()));
		teacher.setSubjects(teacher.getSubjects().stream().map(subject -> subjectService.findById(subject.getId()))
				.collect(Collectors.toList()));
		teacherService.save(teacher);
		logger.debug("Create new teacher. Id {}", teacher.getId());

		return "redirect:/teachers";
	}

	@GetMapping("/{id}/edit")
	public String editTeacher(@PathVariable int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());
		logger.debug("Show edit teacher page");

		return "teachers/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute Teacher teacher, @PathVariable("id") int id) {
		logger.debug("Update teacher with id {}", id);
		teacher.setCathedra(cathedraService.findById(teacher.getCathedra().getId()));
		teacher.setSubjects(teacher.getSubjects().stream().map(subject -> subjectService.findById(subject.getId()))
				.collect(Collectors.toList()));
		teacherService.save(teacher);

		return "redirect:/teachers";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		logger.debug("Delete teacher with id {}", id);
		teacherService.deleteById(id);

		return "redirect:/teachers";
	}

	@GetMapping("/{id}/shedule")
	public ModelAndView showShedule(@PathVariable int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(id));

		return new ModelAndView("teachers/calendar");
	}

	@GetMapping("/{id}/shedule/events")
	public @ResponseBody String getLecturesByTeacherId(@PathVariable int id) {
		ObjectMapper mapper = JsonMapper.builder()
				.findAndAddModules()
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();
		List<Lecture> lectures = lectureService.findByTeacherId(id);
		List<Map<String, Object>> values = new ArrayList<>();
		for (Lecture lecture : lectures) {
			Map<String, Object> element = new HashMap<>();
			element.put("title", lecture.getSubject().getName());
			element.put("start", lecture.getDate().atTime(lecture.getTime().getStart()));
			element.put("end", lecture.getDate().atTime(lecture.getTime().getEnd()));
			element.put("url", "/university/lectures/" + lecture.getId());
			values.add(element);
		}
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(values);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
