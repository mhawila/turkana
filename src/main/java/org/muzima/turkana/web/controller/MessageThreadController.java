package org.muzima.turkana.web.controller;

import org.muzima.turkana.model.MessageThread;
import org.muzima.turkana.service.MessageThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.muzima.turkana.web.controller.MessageThreadController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class MessageThreadController {

    public static final String BASE_PATH = "api/thread";

    @Autowired
    MessageThreadService messageThreadService;

    @PostMapping(consumes = "application/json")
    public void saveMessageThread(@RequestBody MessageThread messageThread){
        messageThreadService.saveMessageThread(messageThread);
    }

    @PostMapping(consumes = "application/json")
    public void saveMessageThreads(@RequestBody List<MessageThread> messageThreadList){
        messageThreadService.saveMessageThreads(messageThreadList);
    }


    @GetMapping(value = "/{uuid}",produces = "application/json")
    public MessageThread getMessageThread(@PathVariable(required = true) String uuid){
        return messageThreadService.getMessageThread(uuid);
    }

    @GetMapping(produces = "application/json")
    public List<MessageThread> getAllMessageThreads(){
        return messageThreadService.getAllMessageThreads();
    }

    @PutMapping(consumes = "application/json")
    public MessageThread updateMessageThread(@RequestBody MessageThread messageThread){
        return messageThreadService.updateMessageThread(messageThread);
    }

    @DeleteMapping("/{uuid}")
    public void deleteMessageThread(@PathVariable(required = true) String uuid){
        messageThreadService.deleteMessageThread(uuid);
    }
}
