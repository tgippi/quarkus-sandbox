package de.tgi.quarkus.sandbox.handler;

import de.tgi.quarkus.sandbox.commands.CreateAddress;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.inject.Inject;

public class AxonCommandDispatcher {

    @Inject
    CommandGateway commandGateway;

    @Incoming("my-data-stream")
    public void logPrice(Integer price) {
        commandGateway.sendAndWait(new CreateAddress("Ewaldstr."));
    }

}
