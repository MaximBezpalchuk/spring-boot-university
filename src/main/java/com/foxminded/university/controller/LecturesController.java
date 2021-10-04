package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.service.LectureService;

@Controller
@RequestMapping("/lectures")
public class LecturesController {

	@Autowired
	LectureService lectureService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("lectures", lectureService.findAll());
		return "lectures/index";
	}
}
