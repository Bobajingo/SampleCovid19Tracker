package io.javabrains.coronavirustracker.models;

public class LocationStats {

    private String country;
    private int latestTotalCases;

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public int getLatestTotalCases(){
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases){
        this.latestTotalCases = latestTotalCases;
    }

    @Override
    public String toString(){
        return "LocationStats{" +
                "country= '" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }


}
