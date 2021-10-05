package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Cathedra;
import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.HolidayService;

@Controller
@RequestMapping("/holidays")
public class HolidaysController {

	@Autowired
	HolidayService holidayService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("holidays", holidayService.findAll());
		return "holidays/index";
	}
	
	@GetMapping("/new")
	public String newHoliday(Model model) {
		model.addAttribute("holiday", Holiday.builder().build());
		return "holidays/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("holiday") Holiday holiday, Model model) {
		holiday.setCathedra(Cathedra.builder().id(1).build());
		holidayService.save(holiday);
		return "redirect:/holidays";
	}
}
