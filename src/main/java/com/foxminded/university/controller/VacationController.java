package com.foxminded.university.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.foxminded.university.model.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.foxminded.university.model.Lecture;
import com.foxminded.university.model.Vacation;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.TeacherService;
import com.foxminded.university.service.VacationService;

@Controller
@RequestMapping("/teachers/{teacherId}/vacations")
public class VacationController {

	private static final Logger logger = LoggerFactory.getLogger(VacationController.class);

	private TeacherService teacherService;
	private VacationService vacationService;
	private LectureService lectureService;

	public VacationController(TeacherService teacherService, VacationService vacationService,
			LectureService lectureService) {
		this.teacherService = teacherService;
		this.vacationService = vacationService;
		this.lectureService = lectureService;
	}

	@GetMapping
	public String all(@PathVariable int teacherId, Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Vacation> page = vacationService
				.findByTeacherId(pageable, teacherId);
		model.addAttribute("vacations", page);
		model.addAttribute("teacher", teacherService.findById(teacherId));

		return "teachers/vacations/index";
	}

	@GetMapping("/{id}")
	public String showVacation(@PathVariable int id, Model model) {
		logger.debug("Show vacation page with id {}", id);
		model.addAttribute("vacation", vacationService.findById(id));

		return "teachers/vacations/show";
	}

	@GetMapping("/new")
	public String newTeacherVacation(@PathVariable int teacherId, Vacation vacation, Model model) {
		logger.debug("Show create page");
		model.addAttribute("teacher", teacherService.findById(teacherId));

		return "teachers/vacations/new";
	}

	@PostMapping
	public String createVacation(@PathVariable int teacherId, @ModelAttribute Vacation vacation) {
		vacation.setTeacher(teacherService.findById(teacherId));
		List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
				vacation.getEnd());
		if (lectures.isEmpty()) {
			vacationService.save(vacation);
			logger.debug("Create new vacation. Id {}", vacation.getId());

			return "redirect:/teachers/" + teacherId + "/vacations";
		} else {
			logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");

			return "redirect:/teachers/" + teacherId + "/vacations/lectures?start=" + vacation.getStart() + "&end="
					+ vacation.getEnd();
		}
	}

	@GetMapping("/lectures")
	public String changeTeacherOnLectures(@PathVariable int teacherId, Model model,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, start, end);
		model.addAttribute("teacher", teacherService.findById(teacherId));
		model.addAttribute("lectures", lectures);
		model.addAttribute("start", start);
		model.addAttribute("end", end);

		return "teachers/vacations/lectures";
	}

	@PatchMapping("/lectures")
	public String autofillTeachersOnLectures(@PathVariable int teacherId, Model model,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
		List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, start, end);
		for (Lecture lecture : lectures) {
			List<Teacher> teachers = teacherService.findTeachersForChange(lecture);
			Random rand = new Random();
			lecture.setTeacher(teachers.get(rand.nextInt(teachers.size()))); // set random free teacher
			lectureService.save(lecture);
		}

		return "redirect:/teachers/" + teacherId + "/vacations";
	}

	@GetMapping("/{id}/edit")
	public String editVacation(@PathVariable int teacherId, @PathVariable int id, Model model) {
		model.addAttribute("teacher", teacherService.findById(teacherId));
		model.addAttribute("vacation", vacationService.findById(id));
		logger.debug("Show edit vacation page");

		return "teachers/vacations/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute Vacation vacation, @PathVariable int teacherId, @PathVariable int id) {
		vacation.setTeacher(teacherService.findById(teacherId));
		List<Lecture> lectures = lectureService.findByTeacherIdAndPeriod(teacherId, vacation.getStart(),
				vacation.getEnd());
		if (lectures.isEmpty()) {
			logger.debug("Update vacation with id {}", id);
			vacationService.save(vacation);

			return "redirect:/teachers/" + teacherId + "/vacations";
		} else {
			logger.debug("Vacation wasn`t created! Need to change teacher in some lectures");

			return "redirect:/teachers/" + teacherId + "/vacations/lectures?start=" + vacation.getStart() + "&end="
					+ vacation.getEnd();
		}
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int teacherId, @PathVariable int id) {
		logger.debug("Delete vacation with id {}", id);
		vacationService.deleteById(id);

		return "redirect:/teachers/" + teacherId + "/vacations";
	}
}
