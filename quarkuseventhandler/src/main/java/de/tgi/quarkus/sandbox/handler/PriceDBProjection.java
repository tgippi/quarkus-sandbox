package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.entity.PriceEntity;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class PriceDBProjection {

    @Inject
    private EntityManager em;

    @Transactional
    @Incoming("my-data-stream")
    public void savePrice(Integer price) {
        System.out.println("Save: " + price);
        em.persist(new PriceEntity(price));
    }
}
