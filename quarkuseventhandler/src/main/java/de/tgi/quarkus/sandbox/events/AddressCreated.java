package de.tgi.quarkus.sandbox.events;

public class AddressCreated {

    private String id;

    private String street;

    public AddressCreated(String id, String street) {
        this.id = id;
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
