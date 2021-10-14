package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.foxminded.university.service.CathedraService;

@Controller
public class HomePageController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);
	
	private CathedraService cathedraService;
	
	public HomePageController(CathedraService cathedraService) {
		this.cathedraService = cathedraService;
	}

	@GetMapping
	public String getMainMenu(Model model) {
		logger.debug("Show index page");
		model.addAttribute("cathedraName", cathedraService.findById(1).getName());
		
		return "index";
	}
}
