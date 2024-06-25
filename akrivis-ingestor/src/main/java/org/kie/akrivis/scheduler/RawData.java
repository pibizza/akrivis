package org.kie.akrivis.scheduler;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

@Entity
@Table(name = "raw_data")
public class RawData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "raw_data_id_seq")
    @SequenceGenerator(name = "raw_data_id_seq", sequenceName = "raw_data_id_seq", allocationSize = 1)
    public long id;

    @ColumnTransformer(write = "?::jsonb")
    public String data;
    @Column(name = "created_at")
    public Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "fk_job")
    public Job job;

}
