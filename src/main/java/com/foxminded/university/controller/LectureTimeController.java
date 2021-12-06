package com.foxminded.university.controller;

import com.foxminded.university.model.LectureTime;
import com.foxminded.university.service.LectureTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lecturetimes")
public class LectureTimeController {

	private static final Logger logger = LoggerFactory.getLogger(LectureTimeController.class);

	private LectureTimeService lectureTimeService;

	public LectureTimeController(LectureTimeService lectureTimeService) {
		this.lectureTimeService = lectureTimeService;
	}

	@GetMapping
	public String getAllLectureTimes(Model model) {
		logger.debug("Show index page");
		model.addAttribute("lectureTimes", lectureTimeService.findAll());

		return "lecturetimes/index";
	}

	@GetMapping("/{id}")
	public String showLectureTime(@PathVariable int id, Model model) {
		logger.debug("Show lecture time page with id {}", id);
		model.addAttribute("lectureTime", lectureTimeService.findById(id));

		return "lecturetimes/show";
	}

	@GetMapping("/new")
	public String newLecture(LectureTime lectureTime, Model model) {
		logger.debug("Show create page");
		return "lecturetimes/new";
	}

	@PostMapping
	public String create(@ModelAttribute LectureTime lectureTime) {
		lectureTimeService.save(lectureTime);
		logger.debug("Create new lecture time. Id {}", lectureTime.getId());

		return "redirect:/lecturetimes";
	}

	@GetMapping("/{id}/edit")
	public String editLectureTime(@PathVariable int id, Model model) {
		model.addAttribute("lectureTime", lectureTimeService.findById(id));
		logger.debug("Show edit lecture time page");

		return "lecturetimes/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute LectureTime lectureTime, @PathVariable int id) {
		logger.debug("Update lecture time with id {}", id);
		lectureTimeService.save(lectureTime);

		return "redirect:/lecturetimes";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable int id) {
		logger.debug("Delete lecture time with id {}", id);
		lectureTimeService.deleteById(id);

		return "redirect:/lecturetimes";
	}
}
