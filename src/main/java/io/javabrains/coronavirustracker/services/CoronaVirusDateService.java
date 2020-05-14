package io.javabrains.coronavirustracker.services;

import io.javabrains.coronavirustracker.models.LocationStats;
import io.javabrains.coronavirustracker.support.AmericanFormatDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import java.lang.InterruptedException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoronaVirusDateService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/" + AmericanFormatDate.getYesterdaysDateInAmericanFormat() + ".csv";
    private List<LocationStats> allStats = new ArrayList<>();

    @PostConstruct   //run when program called
    @Scheduled(cron = "* * * * * *")  //run every second
    public void fetchVirusData() throws IOException, InterruptedException{
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyHeader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyHeader);
        for (CSVRecord record : records) {
            LocationStats locationStat = new LocationStats();
            String currentCountry = record.get("Country_Region");
            locationStat.setCountry(currentCountry);
            int totalCasesForCurrentCountry = Integer.parseInt(record.get("Confirmed"));
            locationStat.setLatestTotalCases(totalCasesForCurrentCountry);
            if(!containsCountry(newStats, currentCountry))
                newStats.add(locationStat);
            else{
                //update total in already existing country
                for(LocationStats currentLocationStat : newStats){
                    if(currentLocationStat.getCountry().equals(currentCountry)){
                        int old = currentLocationStat.getLatestTotalCases();
                        currentLocationStat.setLatestTotalCases(old + totalCasesForCurrentCountry);
                    }
                }

            }
        }
        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats(){
        return allStats;
    }

    public boolean containsCountry(final List<LocationStats> newStats, final String currentCountry){
        return newStats.stream().map(LocationStats::getCountry).filter(currentCountry::equals).findFirst().isPresent();
    }
}
