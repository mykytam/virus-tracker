package io.mykytam.virustracker.models;

public class RecoveredList {
    private int recovered;

    public int getRecovered() {
        return recovered;
    }

    public void setRecovered(int recovered) {
        this.recovered = recovered;
    }


    @Override
    public String toString() {
        return "LocationRecovered{" +
                "recovered=" + recovered +
                '}';
    }
}
