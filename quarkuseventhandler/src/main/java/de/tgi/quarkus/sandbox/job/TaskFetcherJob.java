package de.tgi.quarkus.sandbox.job;

import de.tgi.quarkus.sandbox.entity.ProcessingStatus;
import de.tgi.quarkus.sandbox.entity.TaskEntity;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskFetcherJob {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private EntityManager em;

    @Inject
    private UserTransaction transaction;

    @Channel("fetched-tasks")
    private Emitter<TaskEntity> tasksEmitter;

    @Scheduled(every = "1s")
    public void fetchAllTasks() throws Exception {
        try {
            List<TaskEntity> tasks = updateStatus(findAll());
            tasks.stream()
                    .peek((task) -> logger.info("Verteile Task {} {}", task.getId(), task.getProcessingStatus()))
                    .forEach(tasksEmitter::send);
        } catch (Exception e) {
            logger.error("Fehler beim Update der Tasks:", e);
            System.exit(1);
        }
    }

    private List<TaskEntity> findAll() {
        return em.createQuery("SELECT t FROM TaskEntity t WHERE t.processingStatus = :status ORDER BY t.id",
                TaskEntity.class)
                .setParameter("status", ProcessingStatus.CREATED)
                .setMaxResults(5000)
                .getResultList();
    }


    private List<TaskEntity> updateStatus(List<TaskEntity> tasks) throws Exception {
        try {
            transaction.begin();
            List<String> taskIds = tasks.stream()
                    .map(TaskEntity::getId)
                    .collect(Collectors.toList());
            em.createQuery("UPDATE TaskEntity t SET t.processingStatus = :status WHERE t.id in (:ids)")
                    .setParameter("ids", taskIds)
                    .setParameter("status", ProcessingStatus.FETCHED)
                    .executeUpdate();
            return tasks.stream().map((task) -> {
                task.setProcessingStatus(ProcessingStatus.FETCHED);
                return task;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Fehler beim aktualisieren des Status {}", e.getMessage());
            throw e;
        } finally {
            em.flush();
            transaction.commit();
        }
    }
}
