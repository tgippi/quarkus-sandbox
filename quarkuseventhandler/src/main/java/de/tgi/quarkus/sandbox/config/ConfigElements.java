package de.tgi.quarkus.sandbox.config;

import de.tgi.quarkus.sandbox.aggregates.AddressAggregate;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;

public class ConfigElements {

    @ApplicationScoped
    @Produces
    public EntityManagerProvider entityManagerProvider(EntityManager em) {
        return new EntityManagerProvider() {
            @Override
            public EntityManager getEntityManager() {
                return em;
            }
        };
    }

    @ApplicationScoped
    @Produces
    public CommandGateway commandGateway(Configuration configuration) {
        return configuration.commandGateway();
    }

    @ApplicationScoped
    @Produces
    public Configuration configuration(EntityManagerProvider emp, TransactionManager transactionManager) {
        return DefaultConfigurer
                .defaultConfiguration()
                .configureAggregate(AggregateConfigurer.jpaMappedConfiguration(AddressAggregate.class, emp))
                .configureTransactionManager((c) -> transactionManager)
                .buildConfiguration();
    }

}
