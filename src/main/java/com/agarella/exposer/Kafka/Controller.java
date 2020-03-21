package com.agarella.exposer.Kafka;
import com.agarella.exposer.Entity.GroupMessage;
import com.agarella.exposer.Entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.agarella.exposer.Repository.GroupMessageRepository;
import com.agarella.exposer.Repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {

    @Autowired
    private GroupMessageRepository repository;
    @Autowired
    private MessageRepository msgRepository;

    @RequestMapping(value = "message", method = RequestMethod.POST)
    public ResponseEntity producer(@RequestParam("value") String message,
                                   @RequestParam("user") String user) {
        Message msg = Message.builder()
                .content(message)
                .user(user)
                .build();
        msgRepository.save(msg);
        return new ResponseEntity(HttpStatus.CREATED);


    }
    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public List<Message> getAllMessages() { //TODO: Add parameter to search all messages that contain a tag
        List<Message> messages = new ArrayList<>();
        Iterable<Message> results = this.msgRepository.findAllByOrderByIdDesc();
        results.forEach(message-> {messages.add(message);});
        return messages;
    }
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