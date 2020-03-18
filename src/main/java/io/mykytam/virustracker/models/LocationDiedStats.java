package io.mykytam.virustracker.models;

public class LocationDiedStats {
    private int died;

    public int getDied() {
        return died;
    }

    public void setDied(int died) {
        this.died = died;
    }

    @Override
    public String toString() {
        return "LocationStats{" +
                ", died=" + died +
                '}';
    }
}
