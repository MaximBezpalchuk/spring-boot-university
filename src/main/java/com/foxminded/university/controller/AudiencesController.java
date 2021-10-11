package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;

@Controller
@RequestMapping("/audiences")
public class AudiencesController {

	@Autowired
	AudienceService audienceService;
	@Autowired
	CathedraService cathedraService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("audiences", audienceService.findAll());

		return "audiences/index";
	}

	@GetMapping("/{id}")
	public String showAudience(@PathVariable("id") int id, Model model) {
		model.addAttribute("audience", audienceService.findById(id));

		return "audiences/show";
	}

	@GetMapping("/new")
	public String newAudience(Audience audience, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "audiences/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("audience") Audience audience, Model model) {
		audience.setCathedra(cathedraService.findById(audience.getCathedra().getId()));
		audienceService.save(audience);

		return "redirect:/audiences";
	}
}
