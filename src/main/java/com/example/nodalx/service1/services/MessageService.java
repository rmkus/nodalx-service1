package com.example.nodalx.service1.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String EXCHANGE_NAME = "microserviceExchange";
    private static final String ROUTING_KEY = "microserviceKey";

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void sendMessage() {
        System.out.println("Service1:Sending ping...");

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "ping");
    }

    @RabbitListener(queues = "microserviceQueue")
    public void receiveMessage(String message) {
        System.out.println("Service1:Received message: " + message);
        if (message.equals("ping")) {
            System.out.println("Service1:Sending pong...");
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "pong");
        }
    }
}
