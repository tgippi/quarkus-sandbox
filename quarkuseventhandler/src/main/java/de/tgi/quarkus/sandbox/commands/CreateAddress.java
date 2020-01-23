package de.tgi.quarkus.sandbox.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public class CreateAddress {

    @TargetAggregateIdentifier
    private String id;

    private String street;

    public CreateAddress(String street) {
        this.id = UUID.randomUUID().toString();
        this.street = street;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
