package com.agarella.exposer.Kafka;
import com.agarella.exposer.Entity.GroupMessage;
import com.agarella.exposer.Entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.agarella.exposer.Kafka.Sender;
import com.agarella.exposer.Repository.GroupMessageRepository;
import com.agarella.exposer.Repository.MessageRepository;
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
    public ResponseEntity producer(@RequestParam("message") String message) {
        kafkaSender.send(message);

        return new ResponseEntity(HttpStatus.CREATED);
    }
    @RequestMapping(value = "messages", method = RequestMethod.GET)
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Iterable<Message> results = this.msgRepository.findAll();
        results.forEach(message-> {messages.add(message);});
        return messages;
    }
    @RequestMapping(value = "groups/getAll", method = RequestMethod.GET)
    public List<GroupMessage> getAllGroups(@RequestParam("group") String groupName) {
        List<GroupMessage> groups = new ArrayList<>();
        Iterable<GroupMessage> results = this.repository.findAll();
        results.forEach(group-> {groups.add(group);});
        return groups;
    }
}
