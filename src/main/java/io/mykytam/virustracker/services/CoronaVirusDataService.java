package io.mykytam.virustracker.services;

import io.mykytam.virustracker.models.LocationDiedStats;
import io.mykytam.virustracker.models.LocationRecovered;
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
    public List<LocationStats> getAllStats() { return allStats; }

    private static String VIRUS_DIED_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Deaths.csv";
    private List<LocationDiedStats> allDiedStats =  new ArrayList<>();
    public List<LocationDiedStats> getAllDiedStats() { return allDiedStats; }

    private static String VIRUS_RECOVERED_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Recovered.csv";
    private List<LocationRecovered> allRecoveredStats =  new ArrayList<>();
    public List<LocationRecovered> getAllRecoveredStats() { return allRecoveredStats; }

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
            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int previousDay = Integer.parseInt(record.get(record.size()-2));
            locationStat.setLatestTotalCases(latestCases); // setting the latest info
            locationStat.setDifferenceFromPrevious(latestCases - previousDay); // setting difference
            newStats.add(locationStat);
        }
        this.allStats =  newStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusDiedData() throws IOException, InterruptedException {
        List<LocationDiedStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DIED_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body()); // reader that parses String
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record:records) {
            LocationDiedStats locationDiedStat = new LocationDiedStats();
            int died = Integer.parseInt(record.get(record.size()-1));
            locationDiedStat.setDied(died);
            newStats.add(locationDiedStat);
        }
        this.allDiedStats =  newStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusRecoveredData() throws IOException, InterruptedException {
        List<LocationRecovered> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_RECOVERED_URL))
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvBodyReader = new StringReader(httpResponse.body()); // reader that parses String
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record:records) {
            LocationRecovered locationRecoveredStat = new LocationRecovered();
            int recovered = Integer.parseInt(record.get(record.size()-1));
            locationRecoveredStat.setRecovered(recovered);
            newStats.add(locationRecoveredStat);
        }
        this.allRecoveredStats = newStats;
    }

}
