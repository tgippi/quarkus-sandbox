package de.tgi.quarkus.sandbox;

import io.reactivex.Flowable;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A bean producing random prices every 5 seconds.
 * The prices are written to a Kafka topic (prices). The Kafka configuration is specified in the application configuration.
 */
@ApplicationScoped
public class PriceGenerator {

    private Random random = new Random();

    @Outgoing("my-data-stream")
    @Broadcast
    public Flowable<Integer> generate() {
        System.out.println("Test Price");
        return Flowable.interval(5, TimeUnit.SECONDS)
                .map(tick -> {
                    System.out.println("Produce Int");
                    return random.nextInt(100);
                });
    }

}
