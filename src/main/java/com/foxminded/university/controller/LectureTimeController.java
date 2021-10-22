package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.service.LectureTimeService;

@Controller
@RequestMapping("/lecturetimes")
public class LectureTimeController {

	private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);

	private LectureTimeService lectureTimeService;

	public LectureTimeController(LectureTimeService lectureTimeService) {
		this.lectureTimeService = lectureTimeService;
	}

	@GetMapping
	public String getAllLectureTimes(Model model) {
		logger.debug("Show index page");
		model.addAttribute("lectureTimes", lectureTimeService.findAll());

		return "lecturetimes/index";
	}

	@GetMapping("/{id}")
	public String showLectureTime(@PathVariable int id, Model model) {
		logger.debug("Show lecture time page with id {}", id);
		model.addAttribute("lectureTime", lectureTimeService.findById(id));

		return "lecturetimes/show";
	}
}
