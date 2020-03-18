package io.mykytam.virustracker.models;

public class LocationStats {

    private String state;
    private String country;
    private int latestTotalCases;
    private  int differenceFromPrevious;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getDifferenceFromPrevious() {
        return differenceFromPrevious;
    }

    public void setDifferenceFromPrevious(int differenceFromPrevious) {
        this.differenceFromPrevious = differenceFromPrevious;
    }


    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                ", differenceFromPrevious=" + differenceFromPrevious +
                '}';
    }


}
