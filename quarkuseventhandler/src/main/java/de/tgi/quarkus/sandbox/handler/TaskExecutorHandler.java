package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.entity.ProcessingStatus;
import de.tgi.quarkus.sandbox.entity.TaskEntity;
import io.quarkus.runtime.ShutdownEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class TaskExecutorHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Long processedCounter = 0L;

    @Incoming("fetched-tasks")
    @Outgoing("computed-tasks")
    public TaskEntity executeTask(TaskEntity task) {
        if (task.getProcessingStatus().equals(ProcessingStatus.CREATED))
            throw new RuntimeException("CREATED STATUS!!");

        logger.info("Handle Task {} {}", task.getId(), task.getProcessingStatus());
        processedCounter++;
        return task;
    }

    public void onShutdown(@Observes ShutdownEvent evt) {
        logger.info("Shutdown... {} Tasks bearbeitet", processedCounter);
    }
}
