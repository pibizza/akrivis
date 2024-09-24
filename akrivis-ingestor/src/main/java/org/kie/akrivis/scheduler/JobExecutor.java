package org.kie.akrivis.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.akrivis.dbmodel.Job;
import org.akrivis.dbmodel.JobRepository;
import org.akrivis.dbmodel.RawData;

import java.time.Instant;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class JobExecutor {

    private static final Logger LOG = Logger.getLogger(JobScheduler.class.getName());

    @Inject
    JobRepository jobRepository;

    @Transactional
    public RawData run(Long jobId, IngestorHttpClient client) {
        final Job jobPersisted = jobRepository.findById(jobId);
        jobPersisted.lastRun = Instant.now();
        LOG.info("Executing job: %d at %s".formatted(jobPersisted.id, jobPersisted.lastRun.toString()));

        final Optional<RawData> latest = jobRepository.findLatestRawDataByJobId(jobPersisted.id);
        final String data = client.fetchData(jobPersisted.endpoint);







        if (latest.isPresent() && jsonEquals(data, latest.get().data)) {
            jobRepository.persist(jobPersisted);
            return latest.get();
        }

        final RawData newRawData = new RawData();

        newRawData.data = data;
        newRawData.job = jobPersisted;
        newRawData.createdAt = jobPersisted.lastRun;

        jobRepository.getEntityManager().persist(newRawData);

        return newRawData;
    }

    private boolean jsonEquals(final String jsonA, final String jsonB) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            final JsonNode a = objectMapper.readTree(jsonA);

            final JsonNode b = objectMapper.readTree(jsonB);

            return a.equals(b);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); // TODO log
        }
    }
}
