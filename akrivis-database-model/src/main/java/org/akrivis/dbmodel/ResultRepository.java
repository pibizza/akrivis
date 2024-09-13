package org.akrivis.dbmodel;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ResultRepository implements PanacheRepository<RunResult> {

    public RunResult latest(long cardId) {
        return getEntityManager()
                .createQuery(
                        "select r from RunResult r where card.id = :cardId and runTime =" +
                                "(select max(runTime) from RunResult where card.id = :cardId)",
                        RunResult.class)
                .setParameter("cardId", cardId)
                .getSingleResult();
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

