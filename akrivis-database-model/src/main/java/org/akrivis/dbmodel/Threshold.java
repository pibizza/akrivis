package org.akrivis.dbmodel;

import jakarta.persistence.*;

@Entity
@Table(name = "threshold")
public class Threshold {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "threshold_id_seq")
    @SequenceGenerator(name = "threshold_id_seq", sequenceName = "threshold_id_seq", allocationSize = 1)
    public long id;

    public String name;
    public Integer value;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    public Card card;
}
