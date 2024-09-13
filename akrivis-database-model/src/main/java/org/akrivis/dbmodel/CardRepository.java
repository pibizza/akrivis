package org.akrivis.dbmodel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {
    public void deleteCard(Long cardId) {
        final Card card = getEntityManager().getReference(Card.class, cardId);
        getEntityManager().remove(card);

        getEntityManager().createQuery("delete from CardConfiguration where id in ( select configurationId from Card where id = :cardId )")
                .setParameter("cardId", cardId)
                .executeUpdate();
    }
}
