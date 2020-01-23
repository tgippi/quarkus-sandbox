package de.tgi.quarkus.sandbox.config;

import io.quarkus.runtime.StartupEvent;
import org.axonframework.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class AxonStarter {

    private Logger logger = LoggerFactory.getLogger("AxonStarter");

    @Inject
    private Configuration config;

    public Boolean isStarted = false;

    public void startAxon(@Observes StartupEvent evt) {
        logger.info("Start axon");
        try {
            config.start();
        } catch (Exception e) {
            logger.error("Error while starting axon", e);
        }

    }



}
