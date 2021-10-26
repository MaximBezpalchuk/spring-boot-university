package com.foxminded.university.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	private StudentService studentService;
	private GroupService groupService;
	private LectureService lectureService;

	public StudentController(StudentService studentService, GroupService groupService, LectureService lectureService) {
		this.studentService = studentService;
		this.groupService = groupService;
		this.lectureService = lectureService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Student> page = studentService.findAll(pageable);
		model.addAttribute("students", page);

		return "students/index";
	}

	@GetMapping("/{id}")
	public String showStudent(@PathVariable int id, Model model) {
		logger.debug("Show student page with id {}", id);
		model.addAttribute("student", studentService.findById(id));

		return "students/show";
	}

	@GetMapping("/new")
	public String newStudent(Student student, Model model) {
		logger.debug("Show create page");
		model.addAttribute("groupsAttribute", groupService.findAll());

		return "students/new";
	}

	@PostMapping
	public String create(@ModelAttribute Student student, Model model) {
		if (student.getGroup().getId() != 0) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);
		logger.debug("Create new student. Id {}", student.getId());

		return "redirect:/students";
	}

	@GetMapping("/{id}/edit")
	public String editStudent(@PathVariable int id, Model model) {
		model.addAttribute("student", studentService.findById(id));
		model.addAttribute("groupsAttribute", groupService.findAll());
		logger.debug("Show edit student page");

		return "students/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute Student student, @PathVariable int id) {
		logger.debug("Update student with id {}", id);
		if (student.getGroup().getId() != 0) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);

		return "redirect:/students";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		logger.debug("Delete student with id {}", id);
		studentService.deleteById(id);

		return "redirect:/students";
	}

	@GetMapping("/{id}/shedule")
	public ModelAndView showShedule(@PathVariable int id, Model model) {
		model.addAttribute("student", studentService.findById(id));

		return new ModelAndView("students/calendar");
	}

	@GetMapping("/{id}/shedule/events")
	public @ResponseBody String getLecturesByStudentId(@PathVariable int id) {
		ObjectMapper mapper = JsonMapper.builder()
				.findAndAddModules()
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();
		List<Lecture> lectures = lectureService.findByStudentId(id);
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
