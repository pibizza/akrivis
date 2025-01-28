package org.kie.akrivis.scheduler;

import org.junit.jupiter.api.Test;
import org.kie.akrivis.clients.BackstageClientImpl;
import org.kie.akrivis.clients.DefaultClientImpl;
import org.kie.akrivis.clients.GitHubClientImpl;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IngestorHttpClientTest {

    @Test
    public void testExisting() {
        assertInstanceOf(GitHubClientImpl.class, IngestorHttpClient.findHttpClient("GitHub"));
        assertInstanceOf(BackstageClientImpl.class, IngestorHttpClient.findHttpClient("Backstage"));
        assertInstanceOf(DefaultClientImpl.class, IngestorHttpClient.findHttpClient("Default"));
    }

    @Test
    public void testMissing() {
        assertThrows(UnsupportedOperationException.class, () -> IngestorHttpClient.findHttpClient("Missing"));
    }
}