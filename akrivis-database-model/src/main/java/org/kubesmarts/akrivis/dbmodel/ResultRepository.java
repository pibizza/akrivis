package org.kubesmarts.akrivis.dbmodel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ResultRepository implements PanacheRepository<RunResult> {

    public Optional<RunResult> latest(long cardId) {
        try{
                return Optional.of(getEntityManager().createQuery(
                         """
                                select r from RunResult r where card.id = :cardId and runTime =
                                (select max(runTime) from RunResult where card.id = :cardId)
                         """,
                                RunResult.class)
                        .setParameter("cardId", cardId)
                        .getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
                return Optional.empty();
        }
    }

    public List<RunResult> history(long cardId) {
        return getEntityManager()
                .createQuery(
                        "select r from RunResult r where card.id = :cardId order by r.runTime desc",
                        RunResult.class)
                .setParameter("cardId", cardId)
                .setMaxResults(100) // TODO needs to be smarter
                .getResultList();
    }

}

