package io.mykytam.virustracker.services;

import io.mykytam.virustracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

// service which gives the data
// when application loads, it's going to make a call to url and fetch the data
@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    private List<LocationStats> allStats = new ArrayList<>();

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    @PostConstruct // telling Spring -  when constructed CoronaVirusDataService, execute this method
    @Scheduled(cron = "* * 1 * * *") // schedule the run of a method on regular bases
    public void fetchVirusData() throws IOException, InterruptedException {
        // makes http call to url, sending the request
        List<LocationStats> newStats = new ArrayList<>(); // not getting error responses while rebuilding
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL)) // creates URI out of String
                .build();

        // getting the response from url, that was converted to String
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString()); // request & body handler (what to do with the body)

        // parsing csv with Apache's commons-csv
        StringReader csvBodyReader = new StringReader(httpResponse.body()); // reader that parses String
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record:records) {
            LocationStats locationStat = new LocationStats();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
            newStats.add(locationStat);
        }
        this.allStats =  newStats;
    }
}
