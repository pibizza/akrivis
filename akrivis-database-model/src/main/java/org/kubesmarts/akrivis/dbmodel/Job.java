package org.kubesmarts.akrivis.dbmodel;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "job_id_seq")
    @SequenceGenerator(name = "job_id_seq", sequenceName = "job_id_seq", allocationSize = 1)
    public long id;

    public String endpoint;

    public String type;

    public String cron;

    @Enumerated(EnumType.STRING)
    public JobStatus status;

    @Column(name = "last_run")
    public Instant lastRun;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Job{");
        sb.append("id=").append(id);
        sb.append(", endpoint='").append(endpoint).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", cron='").append(cron).append('\'');
        sb.append(", status=").append(status);
        sb.append(", lastRun=").append(lastRun);
        sb.append('}');
        return sb.toString();
    }
}
