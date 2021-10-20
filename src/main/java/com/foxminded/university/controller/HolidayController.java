package com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;

@Controller
@RequestMapping("/holidays")
public class HolidayController {

	private static final Logger logger = LoggerFactory.getLogger(HolidayController.class);

	private HolidayService holidayService;

	public HolidayController(HolidayService holidayService) {
		this.holidayService = holidayService;
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
}
