package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.LectureService;

@Controller
@RequestMapping("/lectures")
public class LectureController {

	private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

	private LectureService lectureService;

	public LectureController(LectureService lectureService) {
		this.lectureService = lectureService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		Page<Lecture> page = lectureService.findAll(PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()));
		model.addAttribute("lectures", page);

		return "lectures/index";
	}

	@GetMapping("/{id}")
	public String showLecture(@PathVariable("id") int id, Model model) {
		logger.debug("Show lecture page with id {}", id);
		model.addAttribute("lecture", lectureService.findById(id));

		return "lectures/show";
	}
}
