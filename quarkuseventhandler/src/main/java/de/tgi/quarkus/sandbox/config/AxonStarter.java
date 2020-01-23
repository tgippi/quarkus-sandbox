package de.tgi.quarkus.sandbox.config;

import io.quarkus.runtime.StartupEvent;
import org.axonframework.config.Configuration;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class AxonStarter {

    @Inject
    private Configuration config;

    public void onStart(@Observes StartupEvent evt) {
        config.start();
    }

}
