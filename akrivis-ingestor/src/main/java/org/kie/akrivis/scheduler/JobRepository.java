package org.kie.akrivis.scheduler;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JobRepository implements PanacheRepository<Job> {

    public List<Job> findActiveJobs() {
        return list("status", Sort.ascending("id"), JobStatus.SCHEDULED);
    }

    public List<Job> findDraftJobs() {
        return list("status", JobStatus.DRAFT);
    }

    public List<RawData> findRawDataByJobId(Long jobId) {
        return getEntityManager().createQuery("select r from RawData r where job.id = :jobId order by r.id desc",
                                  RawData.class)
                     .setParameter("jobId", jobId)
                     .getResultList();
    }

    public Optional<RawData> findRawDataById(Long jobId, Long rawDataId) {

        try {
            return Optional.of(getEntityManager().createQuery("select r from RawData r where r.id = :rawDataId and r.job.id = :jobId",
                                              RawData.class)
                                 .setParameter("jobId", jobId)
                                 .setParameter("rawDataId", rawDataId)
                                 .getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    public void deleteJobRawData(Long jobId) {
        getEntityManager().createQuery("delete from RawData r where job.id = :jobId")
                          .setParameter("jobId", jobId)
                          .executeUpdate();
        Job job = getEntityManager().getReference(Job.class, jobId);
        getEntityManager().remove(job);
    }
}
