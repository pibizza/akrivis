package org.kie.akrivis.scheduler;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class SchedulerRepositoryTest {

    @Inject
    JobRepository jobRepository;

    @Test
    @Transactional
    public void testFindActiveJobs() {
        Job job1 = job("endpoint1", "0/5 * * * * ?");
        Job job2 = job("endpoint2", "0/3 * * * * ?");

        jobRepository.persist(job1, job2);

        assertThat(jobRepository.findById(job1.id).endpoint, is("endpoint1"));
        assertThat(jobRepository.findById(job2.id).endpoint, is("endpoint2"));
    }

    @Test
    @Transactional
    public void deleteCascade() {
        Job job1 = job("endpoint1", "0/5 * * * * ?");

        jobRepository.persist(job1);

        RawData rawData1 = rawData(job1, "a");
        RawData rawData2 = rawData(job1, "b");

        jobRepository.getEntityManager().merge(rawData1);
        jobRepository.getEntityManager().merge(rawData2);

        assertThat(jobRepository.findRawDataByJobId(job1.id), hasSize(2));

        jobRepository.deleteJobRawData(job1.id);

        assertThat(jobRepository.findRawDataByJobId(job1.id), hasSize(0));
    }

    @Test
    @Transactional
    public void findRawDetail() {
        Job job1 = job("endpoint1", "0/5 * * * * ?");
        jobRepository.persist(job1);

        Job job2 = job("endpoint2", "0/5 * * * * ?");
        jobRepository.persist(job1);

        RawData rawData1 = rawData(job1, "a");
        rawData1 = jobRepository.getEntityManager().merge(rawData1);

        assertThat(jobRepository.findRawDataById(job1.id, rawData1.id), is(Optional.of(rawData1)));
        assertThat(jobRepository.findRawDataById(job2.id, rawData1.id), is(Optional.empty()));

    }

    private static Job job(String endpoint1, String cron) {
        Job job1 = new Job();
        job1.endpoint = endpoint1;
        job1.type = "GitHub";
        job1.cron = cron;
        job1.status = JobStatus.DRAFT;
        job1.lastRun = Instant.now();
        return job1;
    }

    private static RawData rawData(Job job1, String jsonData) {
        RawData rawData1 = new RawData();
        String jsonString = """
                {"a" : "<JSON_CONTENT>"}
                """;
        rawData1.data = jsonString.replace("<JSON_CONTENT>", jsonData);
        rawData1.job = job1;
        rawData1.createdAt = Instant.now();
        return rawData1;
    }
}