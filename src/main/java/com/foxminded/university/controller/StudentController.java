package com.foxminded.university.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.foxminded.university.model.Student;
import com.foxminded.university.service.GroupService;
import com.foxminded.university.service.StudentService;

@Controller
@RequestMapping("/students")
@PropertySource("classpath:config.properties")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	private StudentService studentService;
	private GroupService groupService;
	@Value("${studentsPageSize}")
	private int pageSize;

	public StudentController(StudentService studentService, GroupService groupService) {
		this.studentService = studentService;
		this.groupService = groupService;
	}

	@GetMapping
	public String getAllPageableStudents(Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
		logger.debug("Show index page");
		int currentPage = page.orElse(1);
        int currentPageSize = size.orElse(pageSize);

        Page<Student> studentPage = studentService.findPaginatedStudents(PageRequest.of(currentPage - 1, currentPageSize));
        model.addAttribute("studentPage", studentPage);
        int totalPages = studentPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "students/index";
	}

	@GetMapping("/{id}")
	public String showStudent(@PathVariable("id") int id, Model model) {
		logger.debug("Show student page with id {}", id);
		model.addAttribute("student", studentService.findById(id));

		return "students/show";
	}

	@GetMapping("/new")
	public String newStudent(Student student, Model model) {
		logger.debug("Show create page");
		model.addAttribute("groupsAttribute", groupService.findAll());

		return "students/new";
	}

	@PostMapping
	public String create(@ModelAttribute("student") Student student, Model model) {
		if (student.getGroup().getId() != 0) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);
		logger.debug("Create new student. Id {}", student.getId());

		return "redirect:/students";
	}

	@GetMapping("/{id}/edit")
	public String editStudent(@PathVariable("id") int id, Model model) {
		model.addAttribute("student", studentService.findById(id));
		model.addAttribute("groupsAttribute", groupService.findAll());
		logger.debug("Show edit student page");

		return "students/edit";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("student") Student student, @PathVariable("id") int id) {
		logger.debug("Update student with id {}", id);
		if (student.getGroup().getId() != 0) {
			student.setGroup(groupService.findById(student.getGroup().getId()));
		}
		studentService.save(student);

		return "redirect:/students";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id) {
		logger.debug("Delete student with id {}", id);
		studentService.deleteById(id);

		return "redirect:/students";
	}
}
