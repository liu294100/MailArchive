package org.apollo.mail.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/templates/{page}")
    public String loadTemplate(@PathVariable String page) {
        // Return the template path relative to the templates directory
        return page; // Just return the page name
    }
} 