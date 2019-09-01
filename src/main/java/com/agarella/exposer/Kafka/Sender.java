package com.agarella.exposer.Kafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Sender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    String kafkaTopic = "java_in_use_topic";
    private final Logger logger = LoggerFactory.getLogger(Sender.class);
    public void send(String message) {

        kafkaTemplate.send(kafkaTopic, message);
        logger.info(String.format("$$ -> Sending -> %s",message));
    }
}
