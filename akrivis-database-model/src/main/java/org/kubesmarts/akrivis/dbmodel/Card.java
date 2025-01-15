package org.kubesmarts.akrivis.dbmodel;

import jakarta.persistence.*;
import org.hibernate.Length;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_id_seq")
    @SequenceGenerator(name = "card_id_seq", sequenceName = "card_id_seq", allocationSize = 1)
    public long id;

    @Column(length = Length.LONG32)
    @ColumnTransformer(write = "?::jsonb")
    public String definition;

    @ManyToOne
    public CardConfiguration configuration;
}
