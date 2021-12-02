package com.foxminded.university.controller;

import com.foxminded.university.model.Subject;
import com.foxminded.university.service.CathedraService;
import com.foxminded.university.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    private SubjectService subjectService;
    private CathedraService cathedraService;

    public SubjectController(SubjectService subjectService, CathedraService cathedraService) {
        this.subjectService = subjectService;
        this.cathedraService = cathedraService;
    }

    @GetMapping
    public String all(Model model, Pageable pageable) {
        logger.debug("Show index page");
        Page<Subject> page = subjectService.findAll(pageable);
        model.addAttribute("subjects", page);

        return "subjects/index";
    }

    @GetMapping("/{id}")
    public String showSubject(@PathVariable int id, Model model) {
        logger.debug("Show subject page with id {}", id);
        model.addAttribute("subject", subjectService.findById(id));

        return "subjects/show";
    }

    @GetMapping("/new")
    public String newSubject(Subject subject, Model model) {
        logger.debug("Show create page");
        model.addAttribute("cathedras", cathedraService.findAll());

        return "subjects/new";
    }

    @PostMapping
    public String create(@ModelAttribute Subject subject, Model model) {
        subject.setCathedra(cathedraService.findById(subject.getCathedra().getId()));
        subjectService.save(subject);
        logger.debug("Create new subject. Id {}", subject.getId());

        return "redirect:/subjects";
    }

    @GetMapping("/{id}/edit")
    public String editSubject(@PathVariable int id, Model model) {
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("subject", subjectService.findById(id));
        logger.debug("Show edit subject page");

        return "subjects/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Subject subject, @PathVariable int id) {
        logger.debug("Update subject with id {}", id);
        subject.setCathedra(cathedraService.findById(subject.getCathedra().getId()));
        subjectService.save(subject);

        return "redirect:/subjects";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        logger.debug("Delete subject with id {}", id);
        subjectService.deleteById(id);

        return "redirect:/subjects";
    }
}
