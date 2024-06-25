package org.kie.akrivis.scheduler;

import org.kie.akrivis.github.GitHubClientImpl;
import org.kie.akrivis.github.MockGithubClientImpl;

@FunctionalInterface
public interface IngestorHttpClient {

    String fetchData(String url);

    static IngestorHttpClient findHttpClient(String type) {
        return switch (type) {
            case "GitHub" -> new GitHubClientImpl();
            case "GitHubMock" -> new MockGithubClientImpl();
            default -> throw new UnsupportedOperationException("Unknown type: " + type);
        };
    }
}
