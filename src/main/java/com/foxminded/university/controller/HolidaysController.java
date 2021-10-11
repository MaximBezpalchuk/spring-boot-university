package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foxminded.university.model.Holiday;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.HolidayService;

@Controller
@RequestMapping("/holidays")
public class HolidaysController {

	@Autowired
	HolidayService holidayService;
	@Autowired
	CathedraService cathedraService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("holidays", holidayService.findAll());
		
		return "holidays/index";
	}
	
	@GetMapping("/{id}")
	public String showHoliday(@PathVariable("id") int id, Model model) {
		model.addAttribute("holiday", holidayService.findById(id));

		return "holidays/show";
	}
	
	@GetMapping("/new")
	public String newHoliday(Holiday holiday, Model model) {
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		
		return "holidays/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("holiday") Holiday holiday, Model model) {
		holiday.setCathedra(cathedraService.findById(holiday.getCathedra().getId()));
		holidayService.save(holiday);
		
		return "redirect:/holidays";
	}
}
