package org.kubesmarts.akrivis.quarkus;

public class Payload {
    private String origin;
    private String data;

    public Payload(){

    }

    public Payload(String origin, String data) {
        this.origin = origin;
        this.data = data;
    }

    public String getOrigin() {
        return origin;
    }

    public String getData() {
        return data;
    }
}
