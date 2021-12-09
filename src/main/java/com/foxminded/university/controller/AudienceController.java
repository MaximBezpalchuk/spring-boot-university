package com.foxminded.university.controller;

import com.foxminded.university.model.Audience;
import com.foxminded.university.service.AudienceService;
import com.foxminded.university.service.CathedraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/audiences")
@Transactional
public class AudienceController {

    private static final Logger logger = LoggerFactory.getLogger(AudienceController.class);

    private final AudienceService audienceService;
    private final CathedraService cathedraService;

    public AudienceController(AudienceService audienceService, CathedraService cathedraService) {
        this.audienceService = audienceService;
        this.cathedraService = cathedraService;
    }

    @GetMapping
    public String getAllAudiences(Model model) {
        logger.debug("Show index page");
        model.addAttribute("audiences", audienceService.findAll());

        return "audiences/index";
    }

    @GetMapping("/{id}")
    public String showAudience(@PathVariable int id, Model model) {
        logger.debug("Show audience page with id {}", id);
        model.addAttribute("audience", audienceService.findById(id));

        return "audiences/show";
    }

    @GetMapping("/new")
    public String newAudience(Audience audience, Model model) {
        logger.debug("Show create page");
        model.addAttribute("cathedras", cathedraService.findAll());

        return "audiences/new";
    }

    @PostMapping
    public String create(@ModelAttribute Audience audience) {
        audience.setCathedra(cathedraService.findById(audience.getCathedra().getId()));
        audienceService.save(audience);
        logger.debug("Create new audience. Id {}", audience.getId());

        return "redirect:/audiences";
    }

    @GetMapping("/{id}/edit")
    public String editAudience(@PathVariable int id, Model model) {
        model.addAttribute("cathedras", cathedraService.findAll());
        model.addAttribute("audience", audienceService.findById(id));
        logger.debug("Show edit audience page");

        return "audiences/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute Audience audience, @PathVariable int id) {
        logger.debug("Update audience with id {}", id);
        audience.setCathedra(cathedraService.findById(audience.getCathedra().getId()));
        audienceService.save(audience);

        return "redirect:/audiences";
    }

    @DeleteMapping("/{id}")
    public String delete(@ModelAttribute Audience audience) {
        logger.debug("Delete audience with id {}", audience.getId());
        audienceService.delete(audience);

        return "redirect:/audiences";
    }
}
