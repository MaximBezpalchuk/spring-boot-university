package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.service.AudienceService;

@Controller
@RequestMapping("/audiences")
public class AudienceController {

	private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

	private AudienceService audienceService;

	public AudienceController(AudienceService audienceService) {
		this.audienceService = audienceService;
	}

	@GetMapping
	public String getAllAudiences(Model model) {
		logger.debug("Show index page");
		model.addAttribute("audiences", audienceService.findAll());

		return "audiences/index";
	}

	@GetMapping("/{id}")
	public String showAudience(@PathVariable int id, Model model) {
		logger.debug("Show audience page with id {}", id);
		model.addAttribute("audience", audienceService.findById(id));

		return "audiences/show";
	}
}
