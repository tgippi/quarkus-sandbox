package de.tgi.quarkus.sandbox.handler;

import org.eclipse.microprofile.reactive.messaging.Incoming;

public class PriceLogger {

    @Incoming("my-data-stream")
    public void logPrice(Integer price) {
        System.out.println("Consumer 1: " + price);
    }

}
