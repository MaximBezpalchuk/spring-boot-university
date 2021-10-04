package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.service.AudienceService;

@Controller
@RequestMapping("/audiences")
public class AudiencesController {

	@Autowired
	AudienceService audienceService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("audiences", audienceService.findAll());
		return "audiences/index";
	}
}
