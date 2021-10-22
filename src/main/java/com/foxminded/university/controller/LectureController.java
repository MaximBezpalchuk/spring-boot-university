package com.foxminded.university.controller;

import java.util.stream.Collectors;

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

import com.foxminded.university.model.Lecture;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.LectureService;
import com.foxminded.university.service.LectureTimeService;
import com.foxminded.university.service.SubjectService;
import com.foxminded.university.service.TeacherService;

@Controller
@RequestMapping("/lectures")
public class LectureController {

	private static final Logger logger = LoggerFactory.getLogger(LectureController.class);

	private LectureService lectureService;
	private GroupService groupService;
	private TeacherService teacherService;
	private AudienceService audienceService;
	private SubjectService subjectService;
	private CathedraService cathedraService;
	private LectureTimeService lectureTimeService;

	public LectureController(LectureService lectureService, GroupService groupService, TeacherService teacherService,
			AudienceService audienceService, SubjectService subjectService, CathedraService cathedraService,
			LectureTimeService lectureTimeService) {
		this.lectureService = lectureService;
		this.groupService = groupService;
		this.teacherService = teacherService;
		this.audienceService = audienceService;
		this.subjectService = subjectService;
		this.cathedraService = cathedraService;
		this.lectureTimeService = lectureTimeService;
	}

	@GetMapping
	public String all(Model model, Pageable pageable) {
		logger.debug("Show index page");
		Page<Lecture> page = lectureService.findAll(pageable);
		model.addAttribute("lectures", page);

		return "lectures/index";
	}

	@GetMapping("/{id}")
	public String showLecture(@PathVariable int id, Model model) {
		logger.debug("Show lecture page with id {}", id);
		model.addAttribute("lecture", lectureService.findById(id));

		return "lectures/show";
	}

	@GetMapping("/new")
	public String newLecture(Lecture lecture, Model model) {
		logger.debug("Show create page");
		model.addAttribute("teachersAttribute", teacherService.findAll());
		model.addAttribute("audiencesAttribute", audienceService.findAll());
		model.addAttribute("timesAttribute", lectureTimeService.findAll());
		model.addAttribute("groupsAttribute", groupService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());

		return "lectures/new";
	}

	@PostMapping
	public String create(@ModelAttribute("lecture") Lecture lecture) {
		lecture.setCathedra(cathedraService.findById(lecture.getCathedra().getId()));
		lecture.setTeacher(teacherService.findById(lecture.getTeacher().getId()));
		lecture.setAudience(audienceService.findById(lecture.getAudience().getId()));
		lecture.setTime(lectureTimeService.findById(lecture.getTime().getId()));
		lecture.setSubject(subjectService.findById(lecture.getSubject().getId()));
		lecture.setGroups(lecture.getGroups().stream().map(group -> groupService.findById(group.getId()))
				.collect(Collectors.toList()));
		lectureService.save(lecture);
		logger.debug("Create new lecture. Id {}", lecture.getId());

		return "redirect:/lectures";
	}

	@GetMapping("/{id}/edit")
	public String editLecture(@PathVariable("id") int id, Model model) {
		model.addAttribute("teachersAttribute", teacherService.findAll());
		model.addAttribute("audiencesAttribute", audienceService.findAll());
		model.addAttribute("timesAttribute", lectureTimeService.findAll());
		model.addAttribute("groupsAttribute", groupService.findAll());
		model.addAttribute("subjectsAttribute", subjectService.findAll());
		model.addAttribute("cathedrasAttribute", cathedraService.findAll());
		model.addAttribute("lecture", lectureService.findById(id));
		logger.debug("Show edit lecture page");

		return "lectures/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("lecture") Lecture lecture, @PathVariable("id") int id) {
		logger.debug("Update lecture with id {}", id);
		lecture.setCathedra(cathedraService.findById(lecture.getCathedra().getId()));
		lecture.setTeacher(teacherService.findById(lecture.getTeacher().getId()));
		lecture.setAudience(audienceService.findById(lecture.getAudience().getId()));
		lecture.setTime(lectureTimeService.findById(lecture.getTime().getId()));
		lecture.setSubject(subjectService.findById(lecture.getSubject().getId()));
		lecture.setGroups(lecture.getGroups().stream().map(group -> groupService.findById(group.getId()))
				.collect(Collectors.toList()));
		lectureService.save(lecture);

		return "redirect:/lectures";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		logger.debug("Delete lecture with id {}", id);
		lectureService.deleteById(id);

		return "redirect:/lectures";
	}
}
