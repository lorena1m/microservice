package com.agarella.exposer.Kafka;
import com.agarella.exposer.Entity.Message;
import com.agarella.exposer.Entity.GroupMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;


@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @KafkaListener(topics = "java_in_use_topic", groupId = "group_id")

    public void consume(String message){


    }
}
