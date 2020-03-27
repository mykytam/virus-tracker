package io.mykytam.virustracker.controllers;

import io.mykytam.virustracker.models.DiedList;
import io.mykytam.virustracker.models.RecoveredList;
import io.mykytam.virustracker.models.ReportedList;
import io.mykytam.virustracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private CoronaVirusDataService coronaVirusDataService;

    @Autowired
    public HomeController(CoronaVirusDataService coronaVirusDataService) {
        this.coronaVirusDataService = coronaVirusDataService;
    }

    @GetMapping("/") // return home template
    public String home(Model model) {
        // getting list of objects, converting them to a stream, mapping to the Integer and summing up
        List<ReportedList> allStats = coronaVirusDataService.getAllStats();
        int totalCases = allStats.stream().mapToInt(ReportedList::getLatestTotalCases).sum();
        int totalNewCases = allStats.stream().mapToInt(ReportedList::getDifferenceFromPrevious).sum();

        List<DiedList> allDiedStats = coronaVirusDataService.getAllDiedStats();
        int totalDiedCases = allDiedStats.stream().mapToInt(DiedList::getDied).sum();

        List<RecoveredList> allRecoveredStats = coronaVirusDataService.getAllRecoveredStats();
        int totalRecoveredCases = allRecoveredStats.stream().mapToInt(RecoveredList::getRecovered).sum();


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
