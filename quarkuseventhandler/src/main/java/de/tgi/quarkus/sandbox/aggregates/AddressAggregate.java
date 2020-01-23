package de.tgi.quarkus.sandbox.aggregates;

import de.tgi.quarkus.sandbox.commands.CreateAddress;
import de.tgi.quarkus.sandbox.events.AddressCreated;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Entity
public class AddressAggregate {

    private static Logger logger = LoggerFactory.getLogger(AddressAggregate.class.getName());

    @Id
    @AggregateIdentifier
    private String id;

    private String street;

    @CommandHandler
    public AddressAggregate(CreateAddress cmd) {
        apply(new AddressCreated(cmd.getId(), cmd.getStreet()));
    }

    @EventHandler
    public void on(AddressCreated evt) {
        this.id = evt.getId();
        this.street = evt.getStreet();
        logger.info("Address Aggregate created");
    }

}
