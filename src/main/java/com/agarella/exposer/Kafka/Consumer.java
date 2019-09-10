package com.agarella.exposer.Kafka;
import com.agarella.exposer.Entity.Message;
import com.agarella.exposer.Entity.GroupMessage;
import com.agarella.exposer.Repository.GroupMessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @Autowired
    private GroupMessageRepository groupRepository;
    @KafkaListener(topics = "java_in_use_topic", groupId = "group_id")

    public void consume(String message){

        logger.info(String.format("$$ -> Consumed Message -> %s",message));
        List<String> parsedList = parseValue(message);
        List<String> repeatedList = getRepeated(parsedList);
        boolean hasNew = populateGroups(repeatedList,parsedList);
        //Start HTTP Request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //Build dummy message
        //TODO: Create new structure for notification messages
        Message msg = Message.builder()
                .content(message)
                .id(0)
                .user("@agarella")
                .build();
        ObjectMapper Obj = new ObjectMapper();

        if(hasNew){
            try {
                String jsonStr = Obj.writeValueAsString(msg);
                headers.setContentType(MediaType.APPLICATION_JSON);
                String resourceUrl
                        = "http://localhost:3000/api/kafka/new"; //TODO: Change to dynamic URL
                HttpEntity<String> request = new HttpEntity<String>(jsonStr, headers);

                ResponseEntity<String> response
                        = restTemplate.postForEntity(resourceUrl,request, String.class);
            }catch (IOException e){
                e.printStackTrace();
            }
        }



    }
    /**
     * @param msg - contains the String message value
     * @return a list of string that represent the tags
     **/
    private List<String> parseValue (String msg){
        String[] results = msg.split(" ");
        List<String> listResults = Arrays.asList(results);
        logger.info("Size of list {}",listResults.size());
        listResults = listResults.stream()
                .distinct()
                .filter(result -> result.contains("#"))
                .collect(Collectors.toList());
        logger.info("Size of hashtag list {}", listResults.size());
        return listResults;
    }

    /**
     *
     * @param initialList - List of strings that contain all the tags
     * @return list of strings that contain the tags that already exists on the database
     */
    private List<String> getRepeated (List<String> initialList){
        Iterable<GroupMessage> allGroups = this.groupRepository.findAll();
        List<String> finalResults = new ArrayList<String>();
        for(GroupMessage group : allGroups) {
            List<String> equalResults = initialList.stream()
                    .filter(result -> {
                        String theGroup = group.getGroup();
                        boolean isRepeated = theGroup.equals(result);
                        return isRepeated;
                    })
                    .collect(Collectors.toList());

            if(finalResults.isEmpty()){
                finalResults = equalResults;
            }else {
                finalResults.addAll(equalResults);
            }
        }
        return finalResults;
    }

    /**
     *
     * @param resultsRepeated - tags that are repeated
     * @param newResults - new tags to coming on the message
     * @return true/false if there were new tags
     */
    private boolean populateGroups (List<String> resultsRepeated, List<String> newResults){
        boolean hasNew = false;
        for(String repeated : resultsRepeated) {
            newResults.remove(repeated);
            GroupMessage groupToAdd = this.groupRepository.findByGroup(repeated);
            groupToAdd.setTotal(groupToAdd.getTotal()+1);
            logger.info("Repeated {} id {}", repeated, groupToAdd.getId());
            this.groupRepository.save(groupToAdd);
        }
        logger.info("Total new {} ",newResults.size());
        for(String result : newResults) {
            GroupMessage newGroup = GroupMessage.builder()
                    .group(result)
                    .total(1)
                    .build();
            this.groupRepository.save(newGroup);
            hasNew =true;
        }
        return hasNew;
    }
}
