package org.kie.akrivis.clients;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.kie.akrivis.scheduler.IngestorHttpClient;

public class BackstageClientImpl implements IngestorHttpClient {

    @Override
    public String fetchData(String url) {

        try (Client client = ClientBuilder.newClient()) {
            return client.
                    target(getBackstageHome() + url).
                    request(MediaType.APPLICATION_JSON).
                    get(String.class);
        }
    }

    private String getBackstageHome() {
        return ConfigProvider.getConfig().getValue("akrivis.backstage.address", String.class);
    }
}
