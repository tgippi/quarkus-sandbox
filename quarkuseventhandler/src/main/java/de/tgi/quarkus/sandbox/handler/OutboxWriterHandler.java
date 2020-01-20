package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.entity.TaskEntity;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.UUID;

public class OutboxWriterHandler {

    @Inject
    private EntityManager em;

    @Transactional
    @Incoming("outbox-stream")
    public void saveTask(String task) {
        em.persist(new TaskEntity(UUID.randomUUID().toString(), task));
    }
}
