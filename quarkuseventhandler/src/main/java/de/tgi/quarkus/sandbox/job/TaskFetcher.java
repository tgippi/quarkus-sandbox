package de.tgi.quarkus.sandbox.job;

import de.tgi.quarkus.sandbox.entity.ProcessingStatus;
import de.tgi.quarkus.sandbox.entity.TaskEntity;
import de.tgi.quarkus.sandbox.handler.TaskExecutorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskFetcher {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private EntityManager em;

    @Inject
    private TaskExecutorHandler handler;

    @Transactional
    public void fetch() {
        try {
            processAllTasks();
        } catch (Exception e) {
            logger.error("Fehler beim Update der Tasks:", e);
        }
    }

    public void processAllTasks() throws Exception {
        List<TaskEntity> executedTasks = findAll().stream()
                /*.peek((task) -> logger.info("Verteile Task {} {}", task.getId(), task.getProcessingStatus()))*/
                .peek(handler::executeTask)
                .collect(Collectors.toList());
        deleteTasks(executedTasks);
    }

    private List<TaskEntity> findAll() {
        return em.createQuery("SELECT t FROM TaskEntity t WHERE t.processingStatus = :status ORDER BY t.id",
                TaskEntity.class)
                .setParameter("status", ProcessingStatus.CREATED)
                .setMaxResults(100)
                .getResultList();
    }

    private void deleteTasks(List<TaskEntity> tasks) throws Exception {
        try {
            List<String> taskIds = tasks.stream()
                    .map(TaskEntity::getId)
                    .collect(Collectors.toList());
            /*em.createQuery("DELETE FROM TaskEntity t WHERE t.id in (:ids)")
                    .setParameter("ids", taskIds)
                    .executeUpdate();*/
            em.createQuery("UPDATE TaskEntity t SET t.processingStatus = :status WHERE t.id in (:ids)")
                    .setParameter("ids", taskIds)
                    .setParameter("status", ProcessingStatus.PROCESSED)
                    .executeUpdate();

            printStats();
        } catch (Exception e) {
            logger.error("Fehler beim aktualisieren des Status {}", e.getMessage());
            throw e;
        } finally {
            em.flush();
        }
    }

    private void printStats() {
        Long created = em.createQuery("SELECT count(t) FROM TaskEntity t WHERE t.processingStatus = :status",
                Long.class)
                .setParameter("status", ProcessingStatus.CREATED)
                .getSingleResult();

        Long processed = em.createQuery("SELECT count(t) FROM TaskEntity t WHERE t.processingStatus = :status",
                Long.class)
                .setParameter("status", ProcessingStatus.PROCESSED)
                .getSingleResult();

        logger.info("############### {} Tasks bearbeitet, {} Tasks übrig", processed, created);
    }
}
