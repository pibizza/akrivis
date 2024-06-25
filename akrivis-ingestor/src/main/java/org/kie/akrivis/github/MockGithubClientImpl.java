package org.kie.akrivis.github;

import org.kie.akrivis.scheduler.IngestorHttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// TODO this shouldn't exist, we don't want it to leak to production see db/migration/V1__init_database.sql:17
public class MockGithubClientImpl implements IngestorHttpClient {
    @Override
    public String fetchData(String url) {
        try {
            Path path = Path.of("src/test/resources/example-github-issues-payload.json");
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
