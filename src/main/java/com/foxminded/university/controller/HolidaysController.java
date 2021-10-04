package com.foxminded.university.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
