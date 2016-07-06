package com.example;

import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Optional;

/**
 * Transforms Camel messages. Assumes that message body is a {@link Event} serialized to JSON.
 */
@Component
public class Transformer { // Damn you Camel! Class has to be public
    private static final Logger LOG = LoggerFactory.getLogger(Transformer.class);
    private final ObjectMapper objectMapper;

    @Autowired
    public Transformer(ObjectMapper objectMapper) {
        Assert.notNull(objectMapper);
        this.objectMapper = objectMapper;
    }

    public Optional<String> transform(Exchange ex) {
        try {
            Event event = objectMapper.readValue(ex.getIn(Message.class)
                                                   .getBody(Record.class)
                                                   .getData()
                                                   .array(),
                                                 Event.class);
            return Optional.of(event.eventType);
        } catch (IOException e) {
            LOG.error("Error during deserializing event: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private static class Event {
        private final String eventType;

        @JsonCreator
        private Event(@JsonProperty("event_type") String eventType) {
            this.eventType = eventType;
        }
    }
}
