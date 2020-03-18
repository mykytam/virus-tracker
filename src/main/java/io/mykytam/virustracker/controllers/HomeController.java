package io.mykytam.virustracker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // return home template
    public String home() {
        return "home";
    }
}
