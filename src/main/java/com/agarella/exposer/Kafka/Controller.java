package com.agarella.exposer.Kafka;
import com.agarella.exposer.Entity.GroupMessage;
import com.agarella.exposer.Entity.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.agarella.exposer.Kafka.Sender;
import com.agarella.exposer.Repository.GroupMessageRepository;
import com.agarella.exposer.Repository.MessageRepository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    Sender kafkaSender;
    @Autowired
    private GroupMessageRepository repository;
    @Autowired
    private MessageRepository msgRepository;

    @RequestMapping(value = "message", method = RequestMethod.POST)
    public ResponseEntity producer(@RequestParam("value") String message,
                                   @RequestParam("user") String user) {
        kafkaSender.send(message);
        Message msg = Message.builder()
                .content(message)
                .user(user)
                .build();
        msgRepository.save(msg);
        //Start HTTP Request
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        ObjectMapper Obj = new ObjectMapper();
        try {
            String jsonStr = Obj.writeValueAsString(msg);
            headers.setContentType(MediaType.APPLICATION_JSON);
            String resourceUrl
                    = "http://localhost:3000/api/kafka/send";//TODO: Change to dynamic URL
            HttpEntity<String> request = new HttpEntity<String>(jsonStr, headers);

            ResponseEntity<String> response
                    = restTemplate.postForEntity(resourceUrl,request, String.class);
            return new ResponseEntity(HttpStatus.CREATED);
        }catch (IOException e){
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public List<Message> getAllMessages() { //TODO: Add parameter to search all messages that contain a tag
        List<Message> messages = new ArrayList<>();
        Iterable<Message> results = this.msgRepository.findAllByOrderByIdDesc();
        results.forEach(message-> {messages.add(message);});
        return messages;
    }
    //Test comment
    @RequestMapping(value = "groups/getAll", method = RequestMethod.GET)
    public List<GroupMessage> getAllGroups(@RequestParam(value = "group", required = false) String groupName) {
        List<GroupMessage> groups = new ArrayList<>();
        if(groupName == null) {
            Iterable<GroupMessage> results = this.repository.findAll();
            results.forEach(group -> {
                groups.add(group);
            });
        }else {
            List<GroupMessage> results = this.repository.findByGroupIgnoreCaseContaining(groupName);
            groups.addAll(results);
        }
        return groups;
    }
    @RequestMapping(value = "groups/getTop5", method = RequestMethod.GET)
    public List<GroupMessage> getTopGroups() {
        List<GroupMessage> groups = new ArrayList<>();
        Iterable<GroupMessage> results = this.repository.findTop5ByOrderByTotalDesc();
        results.forEach(group-> {groups.add(group);});
        return groups;
    }
}
