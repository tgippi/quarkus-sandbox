package de.tgi.quarkus.sandbox.job;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.context.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TaskFetcherStarter {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    private ThreadContext threadContext;

    @Inject
    private TaskFetcher taskFetcher;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    void onStart(@Observes StartupEvent ev) {
        logger.info("BEGIN: Start Polling Schedule");
        executorService.scheduleWithFixedDelay(() ->
                threadContext.contextualRunnable(() -> taskFetcher.fetch()).run(),0, 150, TimeUnit.MILLISECONDS);
        logger.info("END: Start Polling Schedule");
    }

    void onStop(@Observes ShutdownEvent ev) {
        executorService.shutdown();
    }

}
