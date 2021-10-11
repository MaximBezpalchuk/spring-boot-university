package com.foxminded.university.controller;

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

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;

@Controller
@RequestMapping("/lecturetimes")
public class LectureTimesController {

	@Autowired
	LectureTimeService lectureTimeService;

	@GetMapping()
	public String index(Model model) {
		model.addAttribute("lectureTimes", lectureTimeService.findAll());

		return "lecturetimes/index";
	}

	@GetMapping("/{id}")
	public String showLectureTime(@PathVariable("id") int id, Model model) {
		model.addAttribute("lectureTime", lectureTimeService.findById(id));

		return "lecturetimes/show";
	}

	@GetMapping("/new")
	public String newLecture(LectureTime lectureTime, Model model) {
		return "lecturetimes/new";
	}

	@PostMapping()
	public String create(@ModelAttribute("lectureTime") LectureTime lectureTime) {
		lectureTimeService.save(lectureTime);

		return "redirect:/lecturetimes";
	}
	
	@GetMapping("/{id}/edit")
	public String editLectureTime(@PathVariable("id") int id, Model model) {
		model.addAttribute("lectureTime", lectureTimeService.findById(id));

		return "lecturetimes/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("lectureTime") LectureTime lectureTime, @PathVariable("id") int id) {
		lectureTimeService.save(lectureTime);

		return "redirect:/lecturetimes";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		lectureTimeService.deleteById(id);

		return "redirect:/lecturetimes";
	}
}
