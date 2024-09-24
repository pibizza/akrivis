package org.kie.akrivis.clients;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import org.kie.akrivis.scheduler.IngestorHttpClient;

public class DefaultClientImpl implements IngestorHttpClient {

    @Override
    public String fetchData(String url) {
        try (Client client = ClientBuilder.newClient()) {
            return client.
                    target(url).
                    request(MediaType.APPLICATION_JSON).
                    get(String.class);
        }
    }
}
