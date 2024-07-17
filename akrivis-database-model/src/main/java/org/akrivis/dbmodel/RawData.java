package org.akrivis.dbmodel;

import jakarta.persistence.*;
import org.hibernate.Length;
import org.hibernate.annotations.ColumnTransformer;

import java.time.Instant;

@Entity
@Table(name = "raw_data")
public class RawData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "raw_data_id_seq")
    @SequenceGenerator(name = "raw_data_id_seq", sequenceName = "raw_data_id_seq", allocationSize = 1)
    public long id;

    @Column(length= Length.LONG32)
    @ColumnTransformer(write = "?::jsonb")
    public String data;
    @Column(name = "created_at")
    public Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "fk_job")
    public Job job;

}
