package com.foxminded.university.controller;

import com.foxminded.university.service.CathedraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomePageController {

    private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);

    private final CathedraService cathedraService;

    public HomePageController(CathedraService cathedraService) {
        this.cathedraService = cathedraService;
    }

    @GetMapping
    public String getMainMenu(Model model) {
        logger.debug("Show index page");
        model.addAttribute("cathedraName", cathedraService.findById(1).getName());

        return "index";
    }
}
