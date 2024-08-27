package org.akrivis.dbmodel.result;

public class Threshold {
    public String name;
    public Number limit;

    public Threshold() {

    }

    public Threshold(String name, Number limit) {
        this.name = name;
        this.limit = limit;
    }
}
