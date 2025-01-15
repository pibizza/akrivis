package org.kubesmarts.akrivis.scheduler;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.kubesmarts.akrivis.dbmodel.Job;
import org.kubesmarts.akrivis.dbmodel.JobRepository;
import org.kubesmarts.akrivis.dbmodel.RawData;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

@QuarkusTest
class JobExecutorTest {

    @Inject
    JobExecutor jobExecutor;

    @Inject
    JobRepository jobRepository;

    @Test
    public void testExecuteHTTPCall() {
        long jobId = 1L;
        String payload = """
                { "data": "rawData" }
                """;

        RawData returnedJob = jobExecutor.run(jobId, type -> payload);
        List<RawData> persistedJob = jobRepository.findRawDataByJobId(jobId);

        assertThat(persistedJob, hasSize(1));
        assertThat(persistedJob.get(0).data, sameJSONAs(payload));
        assertThat(returnedJob.data, sameJSONAs(payload));

        Job byId = jobRepository.findById(jobId);
        assertNotNull(byId.lastRun);
    }

    @Test
    @Transactional
    public void testSaveOnlyDataChange() {
        long jobId = 1L;
        String payload = """
                { "data": "rawData" }
                """;

        Instant firstRunDataCreated = jobExecutor.run(jobId, type -> payload).createdAt;
        Job job = jobRepository.findById(jobId);
        Instant firstRunTime = job.lastRun;

        jobRepository.getEntityManager().refresh(job);

        Instant secondRunDataCreated = jobExecutor.run(jobId, type -> payload).createdAt;
        Instant secondRunTime = job.lastRun;

        List<RawData> persistedJob = jobRepository.findRawDataByJobId(jobId);
        assertThat(persistedJob, hasSize(1));

        assertEquals(firstRunDataCreated, secondRunDataCreated);
        assertTrue(firstRunTime.isBefore(secondRunTime));
    }
}