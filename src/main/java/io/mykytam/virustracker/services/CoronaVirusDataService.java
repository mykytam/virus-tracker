package io.mykytam.virustracker.services;

import io.mykytam.virustracker.models.DiedList;
import io.mykytam.virustracker.models.RecoveredList;
import io.mykytam.virustracker.models.ReportedList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
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

    private static final String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String VIRUS_DIED_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";
    private static final String VIRUS_RECOVERED_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_recovered_global.csv";

    private List<ReportedList> allStats = new ArrayList<>();
    private List<DiedList> allDiedStats = new ArrayList<>();
    private List<RecoveredList> allRecoveredStats = new ArrayList<>();

    public List<ReportedList> getAllStats() {
        return allStats;
    }

    public List<DiedList> getAllDiedStats() {
        return allDiedStats;
    }

    public List<RecoveredList> getAllRecoveredStats() {
        return allRecoveredStats;
    }

    @PostConstruct // telling Spring -  when constructed CoronaVirusDataService, execute this method
    @Scheduled(cron = "0 0 6,19 * * *") // schedule the run of a method on regular bases
    public void fetchVirusData() throws IOException, InterruptedException {
        // makes http call to url, sending the request
        List<ReportedList> newStats = new ArrayList<>(); // not getting error responses while rebuilding

        CSVParser records = getParser(VIRUS_DATA_URL);

        for (CSVRecord record : records) {
            ReportedList locationStat = new ReportedList();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int previousDay = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases); // setting the latest info
            locationStat.setDifferenceFromPrevious(latestCases - previousDay); // setting difference
            newStats.add(locationStat);
        }
        this.allStats = newStats;
    }

    @PostConstruct
    @Scheduled(cron = "0 0 6,19 * * *")
    public void fetchVirusDiedData() throws IOException, InterruptedException {
        List<DiedList> newStats = new ArrayList<>();

        Iterable<CSVRecord> records = getParser(VIRUS_DIED_URL);
        for (CSVRecord record : records) {
            DiedList locationDiedStat = new DiedList();
            int died = Integer.parseInt(record.get(record.size() - 1));
            locationDiedStat.setDied(died);
            newStats.add(locationDiedStat);
        }
        this.allDiedStats = newStats;
    }

    @PostConstruct
    @Scheduled(cron = "0 0 6,19 * * *")
    public void fetchVirusRecoveredData() throws IOException, InterruptedException {
        List<RecoveredList> newStats = new ArrayList<>();

        Iterable<CSVRecord> records = getParser(VIRUS_RECOVERED_URL);
        for (CSVRecord record : records) {
            RecoveredList locationRecoveredStat = new RecoveredList();
            int recovered = Integer.parseInt(record.get(record.size() - 1));
            locationRecoveredStat.setRecovered(recovered);
            newStats.add(locationRecoveredStat);
        }
        this.allRecoveredStats = newStats;
    }

    private CSVParser getParser(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());

        return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new StringReader(send.body()));
    }

}
