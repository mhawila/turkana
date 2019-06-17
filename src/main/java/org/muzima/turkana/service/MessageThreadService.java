package org.muzima.turkana.service;

import org.muzima.turkana.model.MessageThread;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public interface MessageThreadService {

    public void saveMessageThread(MessageThread messageThread);

    public void saveMessageThreads(List<MessageThread> messageThreadList);

    public MessageThread getMessageThread(String uuid);

    public List<MessageThread> getAllMessageThreads();

    public MessageThread updateMessageThread(MessageThread messageThread);

    public void deleteMessageThread(String uuid);

}
