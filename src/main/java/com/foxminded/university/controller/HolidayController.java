package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class HolidayController {

	private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

	private HolidayService holidayService;
	private CathedraService cathedraService;

	public HolidayController(HolidayService holidayService, CathedraService cathedraService) {
		this.holidayService = holidayService;
		this.cathedraService = cathedraService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Holiday> page = holidayService.findAll(pageable);
		model.addAttribute("holidays", page);

		return "holidays/index";
	}

	@GetMapping("/{id}")
	public String showHoliday(@PathVariable int id, Model model) {
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

	@PostMapping
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
