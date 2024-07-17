package org.akrivis.dbmodel;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "run_result")
public class RunResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "run_result_id_seq")
    @SequenceGenerator(name = "run_result_id_seq", sequenceName = "run_result_id_seq", allocationSize = 1)
    public long id;

    public Integer value;

    @Column(name = "run_time")
    public Instant runTime;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    public Card card;
}
