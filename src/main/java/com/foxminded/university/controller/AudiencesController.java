package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Audience;
import com.foxminded.university.model.Cathedra;
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

	@GetMapping("/new")
	public String newAudience(Model model) {
		model.addAttribute("audience", Audience.builder().build());
		return "audiences/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("audience") Audience audience, Model model) {
		audience.setCathedra(Cathedra.builder().id(1).build());
		audienceService.save(audience);
		return "redirect:/audiences";
	}
}
