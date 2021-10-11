package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.HolidayService;

@Controller
@RequestMapping("/holidays")
public class HolidaysController {
	
	private final static Logger logger = LoggerFactory.getLogger(HolidaysController.class);

	@Autowired
	HolidayService holidayService;
	@Autowired
	CathedraService cathedraService;

	@GetMapping()
	public String index(Model model) {
		logger.debug("Show index page");
		model.addAttribute("holidays", holidayService.findAll());

		return "holidays/index";
	}

	@GetMapping("/{id}")
	public String showHoliday(@PathVariable("id") int id, Model model) {
		logger.debug("Show holiday page with id {}", id);
		model.addAttribute("holiday", holidayService.findById(id));

		return "holidays/show";
	}

	@GetMapping("/new")
	public String newHoliday(Holiday holiday, Model model) {
		logger.debug("Show create page");
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "holidays/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("holiday") Holiday holiday, Model model) {
		holiday.setCathedra(cathedraService.findById(holiday.getCathedra().getId()));
		holidayService.save(holiday);
		logger.debug("Create new holiday. Id {}", holiday.getId());

		return "redirect:/holidays";
	}

	@GetMapping("/{id}/edit")
	public String editHoliday(@PathVariable("id") int id, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("holiday", holidayService.findById(id));
		logger.debug("Show edit holiday page");

		return "holidays/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("holiday") Holiday holiday, @PathVariable("id") int id) {
		logger.debug("Update holiday with id {}", id);
		holiday.setCathedra(cathedraService.findById(holiday.getCathedra().getId()));
		holidayService.save(holiday);

		return "redirect:/holidays";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		logger.debug("Delete holiday with id {}", id);
		holidayService.deleteById(id);

		return "redirect:/holidays";
	}
}
