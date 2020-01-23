package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.commands.CreateAddress;
import de.tgi.quarkus.sandbox.config.AxonStarter;
import io.quarkus.scheduler.Scheduled;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class AxonCommandDispatcher {

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Inject
    CommandGateway commandGateway;

    @Inject
    AxonStarter axonStarter;

    @Scheduled(every = "1s")
    public void logPrice() {
        logger.info("Send command");
        commandGateway.sendAndWait(new CreateAddress("Ewaldstr."));
    }

}
