package de.tgi.quarkus.sandbox.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tgi.quarkus.sandbox.aggregates.AddressAggregate;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.AggregateConfigurer;
import org.axonframework.config.Configuration;
import org.axonframework.config.DefaultConfigurer;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.serialization.json.JacksonSerializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import java.util.concurrent.ThreadFactory;

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
    public Configuration configuration(EventStore eventStore, EntityManagerProvider emp,
                                       TransactionManager transactionManager, ObjectMapper objectMapper) {
        return DefaultConfigurer
                .defaultConfiguration()
                .configureAggregate(AggregateConfigurer.jpaMappedConfiguration(AddressAggregate.class, emp))
                .configureTransactionManager((c) -> transactionManager)
                .configureEventStore((c) -> eventStore)
                .configureSerializer((c) -> JacksonSerializer.builder().objectMapper(objectMapper).build())
                .buildConfiguration();
    }

    @ApplicationScoped
    @Produces
    public EventStore eventStore(EntityManagerProvider emp, TransactionManager transactionManager) {
        EventStorageEngine eventStorageEngine = JpaEventStorageEngine.builder()
                .entityManagerProvider(emp)
                .transactionManager(transactionManager)
                .build();

        // eventStorageEngine = new InMemoryEventStorageEngine();

        return EmbeddedEventStore.builder()
                .storageEngine(eventStorageEngine)
                .threadFactory(new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable runnable) {
                        Thread thread = new Thread(runnable);
                        return thread;
                    }
                })
                .build();
    }
}
