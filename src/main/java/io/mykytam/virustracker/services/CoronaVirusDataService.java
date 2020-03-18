package io.mykytam.virustracker.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// service which gives the data
// when application loads, it's going to make a call to url and fetch the data
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    public void fetchVirusData() throws IOException, InterruptedException {
        // makes http call to url
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL)) // creates URI out of String
                .build();

        // sending the request, getting the response from url, that was converted to String
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString()); // request & body handler (what to do with the body)
        System.out.println(httpResponse.body());

    }

}
