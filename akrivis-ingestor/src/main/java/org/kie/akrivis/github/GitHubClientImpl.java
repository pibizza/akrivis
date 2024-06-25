package org.kie.akrivis.github;

import org.kie.akrivis.scheduler.IngestorHttpClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GitHubClientImpl implements IngestorHttpClient {

    @Override
    public String fetchData(String url) {

        HttpClient httpClient = HttpClient.newBuilder()
                                          .version(HttpClient.Version.HTTP_2)  // Set HTTP version
                                          .followRedirects(HttpClient.Redirect.NORMAL) // Set Redirect policy
                                          .connectTimeout(Duration.ofSeconds(20)) // Set connection timeout
                                          .build();

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(url))
                                         .header("Accept", "application/vnd.github+json")
//                                         .header("Authorization", "Bearer " + token)
                                         .header("X-GitHub-Api-Version", "2022-11-28")
                                         .GET()
                                         .build();

        String responseContent = "";
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            responseContent = response.body();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }

        return responseContent;
    }
}
