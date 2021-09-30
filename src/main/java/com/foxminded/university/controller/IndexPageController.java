package com.foxminded.university.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexPageController {

	@GetMapping("/1")
	public String index() {
		return "index";
	}
}
