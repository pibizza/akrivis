package org.kubesmarts.akrivis.scheduler;

import org.kubesmarts.akrivis.clients.DefaultClientImpl;
import org.kubesmarts.akrivis.clients.GitHubClientImpl;
import org.kubesmarts.akrivis.clients.MockGithubClientImpl;

@FunctionalInterface
public interface IngestorHttpClient {

    String fetchData(String url);

    static IngestorHttpClient findHttpClient(String type) {
        return switch (type) {
            case "GitHub" -> new GitHubClientImpl();
            case "Default" -> new DefaultClientImpl();
            case "GitHubMock" -> new MockGithubClientImpl();
            default -> throw new UnsupportedOperationException("Unknown type: " + type);
        };
    }
}
