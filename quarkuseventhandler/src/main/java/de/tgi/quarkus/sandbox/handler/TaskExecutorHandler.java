package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TaskExecutorHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private Long processedCounter = 0L;

    public void executeTask(TaskEntity task) {
        /*logger.info("Handle Task {} {}", task.getId(), task.getProcessingStatus());*/
    }
}
