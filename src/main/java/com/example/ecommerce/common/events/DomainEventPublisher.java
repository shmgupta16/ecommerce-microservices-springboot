package com.example.ecommerce.common.events;

public interface DomainEventPublisher {
    void publish(String topic, Object event);
}
