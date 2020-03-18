package io.mykytam.virustracker.controllers;

import io.mykytam.virustracker.models.LocationDiedStats;
import io.mykytam.virustracker.models.LocationRecovered;
import io.mykytam.virustracker.models.LocationStats;
import io.mykytam.virustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired // getting access to class, to put it's methods in model
    CoronaVirusDataService coronaVirusDataService;


    @GetMapping("/") // return home template
    public String home(Model model) {
        // getting list of objects, converting them to a stream, mapping to the Integer and summing up
        List<LocationStats> allStats = coronaVirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDifferenceFromPrevious()).sum();

        List<LocationDiedStats> allDiedStats = coronaVirusDataService.getAllDiedStats();
        int totalDiedCases = allDiedStats.stream().mapToInt(stat -> stat.getDied()).sum();

        List<LocationRecovered> allRecoveredStats = coronaVirusDataService.getAllRecoveredStats();
        int totalRecoveredCases = allRecoveredStats.stream().mapToInt(stat -> stat.getRecovered()).sum();


        // we can put something in a model
        // then after putting something in this object model, we can access it in html
        // adding attribute  with value
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalCases);
        model.addAttribute("totalReportedNewCases", totalNewCases);
        model.addAttribute("totalDiedStats", totalDiedCases);
        model.addAttribute("totalRecoveredStats", totalRecoveredCases);
        return "home";
    }
}
