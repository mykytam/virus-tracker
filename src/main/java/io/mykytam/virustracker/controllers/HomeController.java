package io.mykytam.virustracker.controllers;

import io.mykytam.virustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired // getting access to class, to put it's methods in model
    CoronaVirusDataService coronaVirusDataService;


    @GetMapping("/") // return home template
    public String home(Model model) {
        // we can put something in a model
        // then after putting something in this object model, we can access it in html
        // adding attribute locationStats with value
        model.addAttribute("locationStats", coronaVirusDataService.getAllStats());
        return "home";
    }
}
