package org.kie.akrivis.scheduler;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.akrivis.dbmodel.Job;
import org.akrivis.dbmodel.JobRepository;
import org.akrivis.dbmodel.RawData;

import java.time.Instant;
import java.util.logging.Logger;

@ApplicationScoped
public class JobExecutor {

    private static final Logger LOG = Logger.getLogger(JobScheduler.class.getName());

    @Inject
    JobRepository jobRepository;

    @Transactional
    public RawData run(Long jobId, IngestorHttpClient client) {
        Job jobPersisted = jobRepository.findById(jobId);
        jobPersisted.lastRun = Instant.now();
        LOG.info("Executing job: %d at %s".formatted(jobPersisted.id, jobPersisted.lastRun.toString()));

        RawData newRawData = new RawData();

        newRawData.data =  client.fetchData(jobPersisted.endpoint);
        newRawData.job = jobPersisted;
        newRawData.createdAt = jobPersisted.lastRun;

        jobRepository.getEntityManager().persist(newRawData);

        return newRawData;
    }
}
