package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;

@Controller
@RequestMapping("/audiences")
public class AudienceController {
	
	private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

	private AudienceService audienceService;
	private CathedraService cathedraService;
	
	public AudienceController(AudienceService audienceService, CathedraService cathedraService) {
		this.audienceService = audienceService;
		this.cathedraService = cathedraService;
	}

	@GetMapping()
	public String index(Model model) {
		logger.debug("Show index page");
		model.addAttribute("audiences", audienceService.findAll());

		return "audiences/index";
	}

	@GetMapping("/{id}")
	public String showAudience(@PathVariable("id") int id, Model model) {
		logger.debug("Show audience page with id {}", id);
		model.addAttribute("audience", audienceService.findById(id));

		return "audiences/show";
	}

	@GetMapping("/new")
	public String newAudience(Audience audience, Model model) {
		logger.debug("Show create page");
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "audiences/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("audience") Audience audience) {
		audience.setCathedra(cathedraService.findById(audience.getCathedra().getId()));
		audienceService.save(audience);
		logger.debug("Create new audience. Id {}", audience.getId());

		return "redirect:/audiences";
	}
	
	@GetMapping("/{id}/edit")
	public String editAudience(@PathVariable("id") int id, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("audience", audienceService.findById(id));
		logger.debug("Show edit audience page");

		return "audiences/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@ModelAttribute("audience") Audience audience, @PathVariable("id") int id) {
		logger.debug("Update audience with id {}", id);
		audience.setCathedra(cathedraService.findById(audience.getCathedra().getId()));
		audienceService.save(audience);
		
		return"redirect:/audiences";
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		logger.debug("Delete audience with id {}", id);
		audienceService.deleteById(id);
		
		return"redirect:/audiences";
	}
}
