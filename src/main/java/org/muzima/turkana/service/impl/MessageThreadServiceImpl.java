package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.MessageThreadRepository;
import org.muzima.turkana.model.MessageThread;
import org.muzima.turkana.service.MessageThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public class MessageThreadServiceImpl implements MessageThreadService {

    @Autowired
    MessageThreadRepository messageThreadRepository;

    @Override
    public void saveMessageThread(MessageThread messageThread) {
        messageThreadRepository.save(messageThread);
    }

    @Override
    public void saveMessageThreads(List<MessageThread> messageThreadList) {
        messageThreadRepository.saveAll(messageThreadList);
    }

    @Override
    public MessageThread getMessageThread(String uuid) {
        return messageThreadRepository.findById(uuid).orElseGet(MessageThread::new);
    }

    @Override
    public List<MessageThread> getAllMessageThreads() {
        return messageThreadRepository.findAll();
    }

    @Override
    public MessageThread updateMessageThread(MessageThread messageThread) {
        return messageThreadRepository.saveAndFlush(messageThread);
    }

    @Override
    public void deleteMessageThread(String uuid) {
        messageThreadRepository.deleteById(uuid);
    }
}
