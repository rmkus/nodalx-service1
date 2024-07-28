package com.example.nodalx.service1.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String EXCHANGE_NAME = "NodalxExchange";
    private static final String ROUTING_KEY_1 = "nodalxeKey1";
    private static final String ROUTING_KEY_2 = "nodalxeKey2";

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    public void sendMessage() {
        System.out.println(LocalDateTime.now().format(formatter)+" - Service1:Sending ping...");

        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_2, "ping");
    }

    @RabbitListener(queues = "nodalxQueue1")
    public void receiveMessage(String message) {
        System.out.println(LocalDateTime.now().format(formatter)+" - Service1:Received message: " + message);
        if (message.equals("ping")) {
            System.out.println(LocalDateTime.now().format(formatter)+" - Service1:Sending pong...");
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY_2, "pong");
        }
    }
}
