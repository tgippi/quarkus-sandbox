package de.tgi.quarkus.sandbox.job;

import de.tgi.quarkus.sandbox.entity.PriceEntity;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class PriceProjectionLoggerJob {

    @Inject
    private EntityManager em;

    @Scheduled(every = "10s")
    public void logAllPrices() {
        System.out.print("Prices persisted: ");
        findAll().forEach((PriceEntity price) -> System.out.print(price.getPrice() + ", "));
        System.out.print('\n');
    }

    private List<PriceEntity> findAll() {
        return em.createQuery("Select p from " + PriceEntity.class.getSimpleName() + " p")
                .getResultList();
    }
}
