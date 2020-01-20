package de.tgi.quarkus.sandbox.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String aggregateId;

    private String payload;

    private ProcessingStatus processingStatus;

    public TaskEntity() {
    }

    public TaskEntity(String aggregateId, String payload) {
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.processingStatus = ProcessingStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getPayload() {
        return payload;
    }

    public ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    public void setProcessingStatus(ProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }
}
