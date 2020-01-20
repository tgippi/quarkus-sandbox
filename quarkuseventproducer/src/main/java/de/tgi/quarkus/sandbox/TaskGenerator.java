package de.tgi.quarkus.sandbox;

import io.quarkus.runtime.StartupEvent;
import io.reactivex.Flowable;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class TaskGenerator {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Outgoing("outbox-stream")
    public Flowable<String> generate() {
        return Flowable.interval(10, TimeUnit.MILLISECONDS)
                .map((tick) -> "Aufgabe " + UUID.randomUUID().toString())
                .map(task -> {
                    // logger.info("Produce Task {}", task);
                    return task;
                });
    }


    public void appKiller(@Observes StartupEvent event) {
        Flowable.interval(15, 1, TimeUnit.SECONDS)
            .doOnEach((tick) -> {
                logger.info("Gewollter Shutdown");
                System.exit(0);
            })
            .subscribe();
    }

}
