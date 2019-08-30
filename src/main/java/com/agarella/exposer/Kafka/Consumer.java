package com.agarella.exposer.Kafka;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "java_in_use_topic")

    public void consume(String message){

        logger.info(String.format("$$ -> Consumed Message -> %s",message));
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:3000/api/kafka/send?topic=socketTopicTest";
        ResponseEntity<String> response
                = restTemplate.po .getForEntity(fooResourceUrl + "/1", String.class);

    }
}
