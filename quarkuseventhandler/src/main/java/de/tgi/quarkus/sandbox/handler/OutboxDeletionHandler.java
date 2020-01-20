package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.entity.ProcessingStatus;
import de.tgi.quarkus.sandbox.entity.TaskEntity;
import io.quarkus.runtime.ShutdownEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class OutboxDeletionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Long remainingCounter = 0L;

    @Inject
    EntityManager em;

    @Transactional
    @Incoming("computed-tasks")
    public void deleteComputedTasks(TaskEntity task) {
        if (task.getProcessingStatus().equals(ProcessingStatus.CREATED))
            throw new RuntimeException("CREATED STATUS!!");

        try {
            TaskEntity taskEntity = em.find(TaskEntity.class, task.getId());
            logger.info("Remove Task {} {}", taskEntity.getId(), taskEntity.getProcessingStatus());
            em.remove(taskEntity);

            remainingCounter = em.createQuery("select count(t) from TaskEntity t", Long.class)
                    .getSingleResult();
            logger.info("{} Tasks übrig", remainingCounter);
        } catch (Exception e) {
            logger.error("Fehler beim entfernen von Task {}, {}", task.getId(), e.getMessage());
            System.exit(1);
        }
    }

    public void onShutdown(@Observes ShutdownEvent evt) {
        logger.info("Shutdown... {} Tasks übrig", remainingCounter);
    }
}
